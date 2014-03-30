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

import scala.actors.Actor
import com.cabrol.francois.mural.generator.rulebased.streaming.{PlayerMessages, MessageBox, StreamGenerator}
import com.cabrol.francois.libjamu.midi.entity.{MidiNoteEvent, Device}
import com.cabrol.francois.atomig.tools.Debug

class StreamPlayer(eventsManager:EventsManager) extends Actor{

  val CLOCK_SYNCHRONIZATION_TICKS_PER_BEAT:Int = 24;
  var deviceSelected:Device = new Device("Gevill", "Gervill", true)
  var opened:Boolean        = false
  val ticksPerBeat:Int      = 4
  val channel               = 1
  val bpmInMillis           = 600000

  def act() {
    Debug.player("Start")
    open()
    if (!opened) {
      Debug.player("The device need to be open")
    }
    if ((CLOCK_SYNCHRONIZATION_TICKS_PER_BEAT % ticksPerBeat) != 0) {
      Debug.player("Ticks per beat (" + ticksPerBeat + ") must be a divider of "
        + CLOCK_SYNCHRONIZATION_TICKS_PER_BEAT + " for MIDI clock synchronization");
    }
    val clockTimingsPerTick:Int = CLOCK_SYNCHRONIZATION_TICKS_PER_BEAT / ticksPerBeat;
    var referenceTime           = System.nanoTime();
    var tickReferenceTime       = referenceTime;
    var timingTickReferenceTime = referenceTime;
    var currentTick             = 0;
    eventsManager.start()
    loop{
        //lazy val currentBeat = math.floor(currentTick / ticksPerBeat)
        Debug.player("Current tick is "  + currentTick)
        Debug.player("Reference time is " + tickReferenceTime)
        referenceTime            = waitNanos(tickReferenceTime, timingTickReferenceTime)
        timingTickReferenceTime += getTimingTickNanos(clockTimingsPerTick, ticksPerBeat)
        if (referenceTime >= tickReferenceTime) {
          eventsManager.askNewNotes(currentTick)
          playTick   (currentTick)
          currentTick = currentTick + 1
        }
        //muteAllChannels;
        tickReferenceTime += getTickNanos(currentTick, ticksPerBeat)
    }
  }

  def playTick(currentTick:Int):Unit = {
    Debug.player("Play tick " + currentTick)
    val midiNotes:List[MidiNoteEvent] = eventsManager.queueOfEvents.filter(p => (currentTick == p.getTick)).toList
    midiNotes.foreach(e => {
      sendMidiMessage(deviceSelected, e.getNoteMessageType(), e.getKey(), e.getVelocity())
      eventsManager.queueOfEvents -= e
    })
  }

  def sendMidiMessage(device:Device, noteMessageType:Int, key:Int, velocity:Int) = Debug.player("Play key:" + key + ", velocity:" + velocity)
//  protected void sendMidiMessage(Device device, int status, int data1, int data2) throws InvalidMidiDataException {
//    ShortMessage sm = new ShortMessage();
//    sm.setMessage(status, channel, data1, data2);
//    device.receiver.send(sm, -1);
//  }

  def muteAllChannel = Debug.player("Mute all channel")

  def waitNanos(time1:Long, time2:Long):Long = {
    val wantedNanos:Long = Math.min(time1, time2)
    val wait:Long        = math.max(0, wantedNanos - System.nanoTime())
    if (wait > 0) {
      Debug.player("Waiting nanos " + wait)
      Thread.sleep((wait / 1000000L).toInt, (wait % 1000000L).toInt)
    }
    else {
      Debug.player("Don't wait")
    }

    wantedNanos
  }

  def getTimingTickNanos(ticksPerBeat:Int, clockTimingsPerTick:Int):Long = {
    60000000000000L / (ticksPerBeat * bpmInMillis * clockTimingsPerTick)
  }

  def getTickNanos(tick:Int, ticksPerBeat:Int):Long = {
    60000000000L / (ticksPerBeat * bpmInMillis)
  }

  def open() {
    if (opened) {
      throw new IllegalStateException("open() already called");
    }

    try {
      deviceSelected.open();
    } catch {
      case _:Throwable => throw new RuntimeException("Could not open MIDI devices")
    }
    opened = true
    Debug.player("The device is open")
  }
}
