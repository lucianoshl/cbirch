for branchingFactor in 75 200 150 100 75 50 25
do
	for maxLeaves in 600 400 800 1000 1200 1400
	do
		echo $branchingFactor $maxLeaves
	done
done

#mvn clean install -DskipTests exec:exec -Dexec.args="-classpath %classpath -Xmx30G -Xss1G -Xoss1G -XX:+UseCompressedOops -XX:-UseGCOverheadLimit -javaagent:/home/void/pesquisa/source/SizeOf.jar br.edu.ufu.comp.pos.db.imageretrieval.framework.Launcher birch normalized oxford $branchingFactor 0 16384"