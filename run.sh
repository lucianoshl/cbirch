nohup mvn clean install exec:exec -Dexec.args="-classpath %classpath  -XX:MaxDirectMemorySize=512m -Xmx6656m -javaagent:$(pwd)/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch oxford 100 2000 3072" &
tail -f nohup.out
