package de.sciss.strugatzki

import java.io.File
import org.scalatest.{GivenWhenThen, FeatureSpec}

class StrugatzkiSuite extends FeatureSpec with GivenWhenThen {
   feature( "The xml representations should be correct" ) {
      info( "Settings and result objects are created," )
      info( "and transformed forward and backward to XML" )

      scenario( "Creating arbitrary configurations" ) {
         given( "A FeatureExtraction.Settings configuration" )
         val fe = new FeatureExtraction.SettingsBuilder
         fe.audioInput     = new File( new File( "testing.aif" ).getAbsolutePath )
         fe.featureOutput  = new File( "relative.aif" )
         fe.metaOutput     = None
         fe.numCoeffs     += 1
         fe.fftSize       += 1
         fe.fftOverlap    += 1
         val fe1 = fe.build
         fe.metaOutput     = Some( fe.audioInput.getParentFile )
         val fe2 = fe.build
         when( "it is transformed to XML and back to the settings object" )
         val fe1t = FeatureExtraction.Settings.fromXML( fe1.toXML )
         val fe2t = FeatureExtraction.Settings.fromXML( fe2.toXML )
         then( "the result should equal the original settings object" )
         assert( fe1 == fe1t )
         assert( fe2 == fe2t )

         given( "A FeatureCorrelation.Settings configuration" )
         val fc = new FeatureCorrelation.SettingsBuilder
         fc.databaseFolder      = new File( "db" ).getCanonicalFile
         fc.metaInput           = new File( "rarara.xml" )
         fc.punchIn             = {
            val old = fc.punchIn
            old.copy( span = Span( old.span.start + 1, old.span.stop + 2 ), temporalWeight = old.temporalWeight + 0.11f )
         }
         fc.punchOut            = Some( FeatureCorrelation.Punch( Span( 555, 666 ), 0.1234f ))
         fc.minPunch += 1
         fc.maxPunch += 2
         fc.normalize           = !fc.normalize
         fc.maxBoost += 1
         fc.numMatches += 1
         fc.numPerFile += 1
         fc.minSpacing += 1
         val fc1 = fc.build
         fc.punchOut    = None
         fc.normalize   = !fc.normalize
         val fc2 = fc.build
         when( "it is transformed to XML and back to the settings object" )
         println( fc1.toXML )
         val fc1t = FeatureCorrelation.Settings.fromXML( fc1.toXML )
         val fc2t = FeatureCorrelation.Settings.fromXML( fc2.toXML )
         println( fc1t.toXML )
         then( "the result should equal the original settings object" )
         assert( fc1 == fc1t )
         assert( fc2 == fc2t )

         given( "A FeatureCorrelation.Match result" )
         val fm1 = FeatureCorrelation.Match( 0.23f, new File( "gaga.aif" ), Span( 33, 44 ), -6f, -7f )
         val fm2 = FeatureCorrelation.Match( 0.46f, new File( "rara.wav" ).getAbsoluteFile, Span( 666, 777 ), 8f, 9f )
         when( "it is transformed to XML and back to the match object" )
         val fm1t = FeatureCorrelation.Match.fromXML( fm1.toXML )
         val fm2t = FeatureCorrelation.Match.fromXML( fm2.toXML )
         then( "the result should equal the original match object" )
         assert( fm1 == fm1t )
         assert( fm2 == fm2t )
      }
   }
}