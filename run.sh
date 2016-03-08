cd $HOME/workspace/birch-experiment
git pull
set +e
mvn clean install exec:exec -Dexec.args="-classpath %classpath -Xmx6656m -javaagent:/home/void/birch-experiment/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch oxford 100 2000 3072"
set -e
curl --header 'Access-Token: o.hBKuEYmUeut5XRrSp9Zr0sB34U1v9Um9' --header 'Content-Type: application/json' --data-binary '{"body":"Experiment end","title":"Experiment end","type":"note"}' --request POST https://api.pushbullet.com/v2/pushes
