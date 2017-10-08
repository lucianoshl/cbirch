for dataset in ukbench-00-1000 ukbench-01-1000 ukbench-02-1000 ukbench-03-1000 ukbench-04-1000 ukbench-05-1000 ukbench-06-1000 ukbench-07-1000 ukbench-08-1000 ukbench-09-1000 ukbench-10-200 ukbench-10200
do
	cbirch_workspace=/workspace DATASET_WORKSPACE=/workspace mvn clean install exec:exec -Dexec.args="-classpath %classpath -Xmx31G -Xss1G -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:$(pwd)/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher $dataset" -DskipTests
done