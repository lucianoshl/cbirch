for threshold in 2 3 25 50 75 100 150 200
do
	mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx6656m -Xss2G -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:/home/void/pesquisa/source/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch normalized oxford $threshold 0 20480"
done


#nohup mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx30G -Xss1G -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:/home/void/pesquisa/source/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch normalized oxford 2 0 20480" &