import java.text.SimpleDateFormat

object sc {
    def usage {
        println("Usage: sc [offset] [subtitle] [regexp]");
    }

    def main(args : Array[String]) {
        if (args.length > 3) {
            return usage
        }
        val fileP = if (args.length < 2) "sbt.srt" else args(1)

        
        io.Source.fromFile(fileP).getLines.foreach((line: String) => {
            val pat = if (args.length < 3) """([0-9]+):([0-9]+):([0-9]+),[0-9]{0,3} --> ([0-9]+):([0-9]+):([0-9]+),[0-9]{3}.*\n""".r else args(2).r
            val normall = """(.*)\n""".r

            val offset = if (args.length > 0) args(0).toInt else 13

            line match {
                case pat(fh,fm,fs,sh,sm,ss) => {
                    val firstTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-03-27 %s:%s:%s".format(fh, fm, fs));
                    val secondTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-03-27 %s:%s:%s".format(sh, sm, ss));

                    firstTime.setTime(firstTime.getTime() + offset * 1000);
                    secondTime.setTime(secondTime.getTime() + offset * 1000);

                    println("%02d:%02d:%02d,300 --> %02d:%02d:%02d,302".format(firstTime.getHours(),firstTime.getMinutes(),firstTime.getSeconds(), secondTime.getHours(), secondTime.getMinutes(), secondTime.getSeconds()))
                }
                case normall(a) => println(a)

            }
        })
    }    
}