run('/home/void/workspace/vlfeat-0.9.20/toolbox/vl_setup')

dataset_path = '/home/void/workspace/workspace-birch/datasets/oxford-100';
features_size = 364778;


bin_file = strcat(dataset_path,'/feat_oxc1_hesaff_sift.bin';
fid = fopen(bin_file);
data = fread(fid, [128, features_size], '*uint8');


K        = 3;
nleaves  = 100;
[tree,A] = vl_hikmeans(data,K,nleaves); 
save('myFile.txt', 'tree', '-ASCII','-append');