run('/home/void/Downloads/vlfeat-0.9.20/toolbox/vl_setup')

featuresFile = '%featuresFile%';
featuresAmount = %featuresAmount%;
branchingFactor = %branchingFactor%;
nLeaves  = %nLeaves% ;
outputFile = '%outputFile%';

fid = fopen(featuresFile);
data = fread(fid, [128, featuresAmount], '*uint8');

[tree,A] = vl_hikmeans(data,branchingFactor,nLeaves) ;

save(outputFile,'tree')