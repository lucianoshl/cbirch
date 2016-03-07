nohup mvn clean install exec:exec -Dexec.args="-classpath %classpath -Xmx6656m -javaagent:$(pwd)/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch oxford 100 1500 3072" &
