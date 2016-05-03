cd $HOME/workspace/dsl-cbir
git pull
set +e
mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx6656m -javaagent:/home/void/workspace/dsl-cbir/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch oxford 100 2000 3072"
set -e
curl --header 'Access-Token: o.hBKuEYmUeut5XRrSp9Zr0sB34U1v9Um9' --header 'Content-Type: application/json' --data-binary '{"body":"Experiment end","title":"Experiment end","type":"note"}' --request POST https://api.pushbullet.com/v2/pushes

 docker run -it -e DATASET_WORKSPACE=/home/lucianos/workspace/luciano/workspace-birch -v $HOME/workspace/luciano/dsl-cbir:/c -v $HOME:$HOME -v /home/lucianos/.m2:/m --rm algar/crm-builder mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx6656m -Xss1G -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:/c/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch normalized oxford 100 0 3072" 