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

package com.cabrol.francois.atomig.player

import scala.collection.mutable.{ArrayBuffer, ListBuffer, SynchronizedBuffer, Buffer}
import com.cabrol.francois.libjamu.midi.entity.MidiNoteEvent
import scala.actors.Actor
import com.cabrol.francois.mural.generator.rulebased.streaming.{StreamGenerator, PlayerMessages, MessageBox}
import com.cabrol.francois.atomig.tools.Debug
import com.cabrol.francois.libjamu.midi.factory.MidiEventFactory
import scala.collection.JavaConverters._

/**
 * Created with IntelliJ IDEA.
 * User: francois * Date: 2014-03-30
 */
class EventsManager(streamGenerator:StreamGenerator) extends Actor{

  var queueOfEvents = new ArrayBuffer[MidiNoteEvent] with SynchronizedBuffer[MidiNoteEvent]

  def act() {
    launchGenerator
    loop{
      react{
        case ev:MessageBox => { Debug.eventsManager("Get new notes from the generator:"+ev.ls);
          if(!ev.ls.isEmpty) {
            val midiNoteEvents:List[MidiNoteEvent] = ev.ls.map(f => MidiEventFactory.getMidiNoteEvents(f).asScala.toList).flatten
            queueOfEvents ++= midiNoteEvents
          }
        }
        case _             => Debug.eventsManager("Received message type not recognized")
      }
    }
  }

  def launchGenerator = streamGenerator.start

  def next = streamGenerator ! PlayerMessages.next

  def askNewNotes(currentTick:Int):Unit = {
    if (!queueOfEvents.isEmpty) {
      Debug.eventsManager("last tick" +  queueOfEvents.last.getTick)
      Debug.eventsManager("(currentTick + streamGenerator.indentTick)" + (currentTick + streamGenerator.indentTick))
    }
    if(queueOfEvents.isEmpty || queueOfEvents.last.getTick < (currentTick + streamGenerator.indentTick)){
      Debug.eventsManager("Ask new notes to the generator")
      next
    }
  }

}
