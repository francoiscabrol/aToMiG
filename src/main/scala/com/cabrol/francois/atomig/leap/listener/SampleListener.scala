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

package com.cabrol.francois.atomig.leap.listener

import javafx.application.Platform
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import com.leapmotion.leap._

/**
 * Created with IntelliJ IDEA.
 * User: francois * Date: 2014-02-11
 */
class SampleListener(val commands:Commands) extends Listener {

  override def onInit(controller: Controller) {
    println("Initialized")
  }

  override def onConnect(controller: Controller) {
    println("Connected")
    controller.enableGesture(Gesture.Type.TYPE_SWIPE)
    controller.enableGesture(Gesture.Type.TYPE_CIRCLE)
    controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP)
    controller.enableGesture(Gesture.Type.TYPE_KEY_TAP)
  }

  override def onDisconnect(controller: Controller) {
    println("Disconnected")
  }

  override def onExit(controller: Controller) {
    println("Exited")
  }

  override def onFrame(controller: Controller) {
    val frame: Frame = controller.frame
    //println("Frame id: " + frame.id + ", timestamp: " + frame.timestamp + ", hands: " + frame.hands.count + ", fingers: " + frame.fingers.count + ", tools: " + frame.tools.count + ", gestures " + frame.gestures.count)
    if (!frame.hands.isEmpty) {
      val hand: Hand = frame.hands.get(0)
      val fingers: FingerList = hand.fingers
      if (!fingers.isEmpty) {
        commands.setNumOfFingers(fingers.count())
      }
      val pointable:Pointable = frame.pointables().frontmost()
      val roll = math.floor(pointable.tipPosition().getY)
      println(roll)
      commands.setRoll(roll)
      commands.setIsPlaying(true)
    }
    else{
        commands.setIsPlaying(false)
        commands.setNumOfFingers(0)
    }
  }
}

class CommandRectangle extends Rectangle {

  super.getStyleClass.add("commandRectangle")

  def setInvertedHeight(v:Double) = {
    super.setY(super.getY + (super.getHeight - v))
    super.setHeight(v)
  }

}

case class Commands(val density:CommandRectangle, val textMessage:Text, val textFingers:Text){

  val commandRange = Range(1, 500)

  def setNumOfFingers(num:Int) = {
    val txt = num + " fingers"
    if(txt != textFingers.getText){
      Platform.runLater (new Runnable() {
        def run = {
          textFingers.setText(num + " fingers")
        }
      })
    }
  }

  def setIsPlaying(b:Boolean) = {
    val txt = if (b) "play" else "break"
    if(txt != textMessage.getText){
      Platform.runLater (new Runnable() {
        def run = {
          textMessage.setText(txt)
        }
      })
    }
  }

  def setRoll(roll:Double) = {
    if (commandRange.contains(roll) && density.getHeight.toInt != roll){
      Platform.runLater (new Runnable() {
        def run = {
          density.setInvertedHeight(roll.toInt)
        }
      })
    }
  }

}