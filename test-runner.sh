for threshold in 3 25 50 75 100 150 200
do
	mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx76 -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:/home/void/workspace/birch-experiment/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch oxford $threshold 0 3072"
done