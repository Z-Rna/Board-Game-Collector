package com.example.boardgamecollector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class XMLParser {
    private var context: Context? = null
    private var baseURL: String = "https://www.boardgamegeek.com/xmlapi2/"
    private val _done: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val done: LiveData<Boolean> get() = _done
    var globalURL: String = ""
    fun run(context: Context){
        this.context = context
        DownloaderXML().execute()

    }

    fun globalURL (url: String) {
        globalURL = baseURL + url
    }

    private inner class DownloaderXML: AsyncTask<String, Int, String>() {
        override fun onPreExecute() {
            _done.postValue(false)
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            _done.postValue(true)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(globalURL)
                Log.i("into", globalURL)
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val path = context!!.getApplicationInfo().dataDir
                val saveDir = File("$path/XML")
                if (!saveDir.exists()) {
                    saveDir.mkdir()
                }
                val yourFile = File("$saveDir/values.xml")
                yourFile.createNewFile(); // if file already exists will do nothing
                val fos = FileOutputStream(yourFile, false)
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)

                while(count != -1) {
                    total += count.toLong()
                    val progress_tmp = total.toInt() * 100 / lengthOfFile
                    if (progress_tmp % 10 == 0 && progress != progress_tmp)
                        progress = progress_tmp
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            }
            catch (e: MalformedURLException) {
                return "Malformed URL"
            }
            catch (e: FileNotFoundException) {
                Log.i("stack", e.stackTraceToString())
                return "File Not Found"
            }
            catch (e: IOException) {
                Log.i("stack", e.stackTraceToString())
                return "IO Exception"
            }

            return "success"
        }
    }

    fun parseFindGame(context: Context): HashMap<String, String> {
        val filename = "values.xml"
        val map: HashMap<String, String> = HashMap()
        val path = context!!.getApplicationInfo().dataDir
        val inDir = File("$path/XML")
        if(inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    Log.i("atr", itemNode.attributes.getNamedItem("id").textContent)
                    var id = itemNode.attributes.getNamedItem("id").textContent
                    val elem = itemNode as Element
                    val child = elem.childNodes
                    for (j in 0 until child.length) {
                        val node = child.item(j)
                        if(node is Element){
                            when(node.nodeName) {
                                "name" -> {
                                    Log.i("child", node.attributes.getNamedItem("value").textContent)
                                    var name = node.attributes.getNamedItem("value").textContent
                                    map[id] = name
                                }

                            }
                        }
                    }
                }
                map["0"] = "None"
            }
        }
        return map
    }

    fun parseGame(context: Context, game: Game = Game()): Triple<Game, ArrayList<Designer>, ArrayList<Artist>> {
        this.context = context
        var listDesigners = ArrayList<Designer>()
        var listArtist = ArrayList<Artist>()
        val filename = "values.xml"
        val path = context!!.getApplicationInfo().dataDir
        val inDir = File("$path/XML")
        if(inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    val id = itemNode.attributes.getNamedItem("id").textContent
                    Log.i("ID", id)
                    game.bgc = id.toInt()
                    val elem = itemNode as Element
                    val child = elem.childNodes
                    for (j in 0 until child.length) {
                        val node = child.item(j)
                        if(node is Element){
                            when(node.nodeName) {
                                "thumbnail" -> {
                                    val thumbnail = node.textContent
                                    Log.i("Thumbnail", thumbnail)
                                    game.url = thumbnail
                                }
                                "name" -> {
                                    when(node.attributes.getNamedItem("type").textContent) {
                                        "primary" -> {
                                            val primary = node.attributes.getNamedItem("value").textContent
                                            Log.i("Primary", primary)
                                            game.originalTitle = primary

                                        }
                                    }
                                }
                                "yearpublished" -> {
                                    val yearpublished = node.attributes.getNamedItem("value").textContent
                                    Log.i("YearPublished", yearpublished)
                                    game.year = yearpublished.toInt()
                                }
                                "description" -> {
                                    val description = node.textContent
                                    Log.i("Description", description)
                                    game.descrition = description
                                }
                                "link" -> {
                                    when(node.attributes.getNamedItem("type").textContent) {
                                        "boardgameartist" -> {
                                            val idArtist = node.attributes.getNamedItem("id").textContent
                                            val nameArtist = node.attributes.getNamedItem("value").textContent
                                            Log.i("idArtist", idArtist)
                                            Log.i("nameArtist", nameArtist)
                                            listArtist.add(Artist(idArtist.toInt(), nameArtist))
                                        }
                                        "boardgamedesigner" -> {
                                            val idDesigner = node.attributes.getNamedItem("id").textContent
                                            val nameDesigner = node.attributes.getNamedItem("value").textContent
                                            Log.i("idDesigner", idDesigner)
                                            Log.i("nameDesigner", nameDesigner)
                                            listDesigners.add(Designer(idDesigner.toInt(), nameDesigner))

                                        }
                                    }
                                }
                                "statistics" -> {
                                    val nodeStats = node.childNodes
                                    val itemStats = nodeStats.item(1)

                                    val nodeRatings = itemStats.childNodes
                                    val itemRatings = nodeRatings.item(7)

                                    val nodeRanks = itemRatings.childNodes
                                    val itemRanks = nodeRanks.item(1)

                                    val statistics = itemRanks.attributes.getNamedItem("value").textContent
                                    Log.i("Stats", statistics)
                                    if (statistics != "Not Ranked")
                                        game.ranking = statistics.toInt()
                                    game.type = "base"
                                }
                            }
                        }
                    }
                }
            }
            inDir.delete()
        }
        return Triple(game, listDesigners, listArtist)
    }

    fun parseUser(context: Context): ArrayList<String>{
//        ronaldsf
        var list = ArrayList<String>()
        val filename = "values.xml"
        val path = context!!.getApplicationInfo().dataDir
        val inDir = File("$path/XML")
        if(inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                xmlDoc.documentElement.normalize()
                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0 until items.length) {
                    val itemNode: Node = items.item(i)
                    var objectdid = itemNode.attributes.getNamedItem("objectid").textContent
                    val elem = itemNode as Element
                    val child = elem.childNodes
                    for (j in 0 until child.length) {
                        val node = child.item(j)
                        if(node is Element){
                            when(node.nodeName) {
                                "status" -> {
                                    if(node.attributes.getNamedItem("own").textContent.toInt() > 0) {
                                        list.add(objectdid)
                                        Log.i("id", objectdid)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list
    }

}