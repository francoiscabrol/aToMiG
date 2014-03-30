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

package com.cabrol.francois.atomig

import com.cabrol.francois.mural.generator.rulebased.parameters._
import com.cabrol.francois.libjamu.musictheory.entity.scaleNote.{Scale, Chord}
import com.cabrol.francois.mural.generator.rulebased.sequential.Methods
import com.cabrol.francois.mural.generator.rulebased.parameters.HarmonicDefinition
import com.cabrol.francois.mural.generator.rulebased.streaming.StreamGenerator
import com.cabrol.francois.atomig.player.{EventsManager, StreamPlayer}

/**
 * Created with IntelliJ IDEA.
 * User: francois * Date: 2013-11-27
 */
object RealTimeGenerator {

    def main(args: Array[String]) {
      val parentNotes = List()
      val chords:Map[Float, HarmonicDefinition] = Map((0, HarmonicDefinition(new Chord("C"), new Scale("C major"))), (2, HarmonicDefinition(new Chord("Am"), new Scale("A minor"))))
      val hP = new HarmonicProgression(chords)
      val generationMethod = Methods.rulesBased
      val numBeatsPerBar = 4
      val numBars = 4
      val ambitus:Ambitus = new Ambitus(40, 100)
      val pSilence = 0
      val percentageOfNoteInChord = 100
      val numOfNoteAtTheSameTimeUnit = 1
      //val melodyCurb = MelodyCurb(numBeatsPerBar*numBars, 4)
      //println(melodyCurb.curb)
      val density = 1
      val variance = 0
      val global = new GlobalParameters(generationMethod, parentNotes, numBeatsPerBar, numBars, ambitus, hP, pSilence, numOfNoteAtTheSameTimeUnit, Direction.up, variance, density, 0, percentageOfNoteInChord)
      val dynamic:List[DynamicParameters] = List()
      val param = Parameters(global, dynamic, 1)

      val q = new StreamGenerator(param)
      val sp = new StreamPlayer(new EventsManager(q))
      sp.start
    }

}
