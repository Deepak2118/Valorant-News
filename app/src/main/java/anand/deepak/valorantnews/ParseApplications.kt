package anand.deepak.valorantnews

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Created by timbuchalka for Android Oreo with Kotlin course
 * from www.learnprogramming.academy
 */
class ParseApplications {
    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name
                when (eventType) {

                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for " + tagName)
                        if (tagName == "item") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for " + tagName)
                        if (inEntry) {
                            when (tagName) {
                                "item" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()   // create a new object
                                }

                                "title" -> currentRecord.title = textValue
                                "pubDate" -> currentRecord.relDate = textValue
                                "description" -> currentRecord.desciption = textValue
                            }
                        }
                    }
                }

                // Nothing else to do.
                eventType = xpp.next()
            }

            for (app in applications) {
                Log.d(TAG,"*******************")
                Log.d(TAG,app.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}