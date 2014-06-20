/*
 * Copyright (c) 2014 François Cabrol.
 *
 *  This file is part of aToMiG.
 *
 *     aToMiG is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     aToMiG is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with aToMiG.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cabrol.francois.atomig.javafx.app


import javafx.application.Application
import javafx.scene.text.Text
import javafx.scene.{Group, Scene}
import javafx.stage.Stage
import com.cabrol.francois.atomig.leap.listener.{Commands, CommandRectangle, SampleListener}
import com.leapmotion.leap.Controller

/**
 * Created with IntelliJ IDEA.
 * User: francois * Date: 2014-02-11
 */
class LeapMotionApp extends Application {

  val s:String = "aaa"
  var listener:SampleListener = null //= new SampleListener(new Commands(new CommandRectangle()))
  val control:Controller = new Controller

  override def start(stage: Stage) = {

    stage.setTitle("Leap Generator");

    val root:Group = new Group();
    val sceneHeight = 600
    val scene  = new Scene(root,800,sceneHeight);

    val r = new CommandRectangle();
    r.setX(50)
    r.setY(sceneHeight - 150)
    r.setRotate(180)
    r.setWidth(200)
    r.setHeight(100)
    r.setArcWidth(20)
    r.setArcHeight(20)

    val textPlayPause = new Text("beak ")
    textPlayPause.setX(scene.getWidth/2)
    textPlayPause.setY(scene.getHeight/2)
    textPlayPause.getStyleClass.add("message")

    val textFingers = new Text("0 finger")
    textFingers.setX(scene.getWidth/2)
    textFingers.setY(20)
    textFingers.getStyleClass.add("fingers")

    val densityText = new Text("Density")
    densityText.setX(60)
    densityText.setY(scene.getHeight/2 + 40)
    densityText.getStyleClass.add("commandName")

    root.getChildren.add(r)
    root.getChildren.add(textPlayPause)
    root.getChildren.add(densityText)
    root.getChildren.add(textFingers)

    stage.setScene(scene);
    scene.getStylesheets().add("/resources/scene.css");
    stage.show();

    val c = Commands(r, textPlayPause, textFingers)
    listener = new SampleListener(c)
    control.addListener(listener)

  }

  override def stop() = {
    control.removeListener(listener)
    //print(c)
  }

}

object LeapMotionAppMain {
  def main(args: Array[String]) = {
    Application.launch(classOf[LeapMotionApp])
  }
}