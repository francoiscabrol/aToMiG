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

package com.cabrol.francois.atomig.tools

/**
 * Created with IntelliJ IDEA.
 * User: francois * Date: 2014-02-17
 */
object Debug {

  val player=true

  def player(msg:String):Unit = if(player) println("[PLAYER] " + msg)

  private def setColor(s:String):String = s match {
    case "blue" => Console.BLUE
  }


}
