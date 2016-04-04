run('/home/void/Downloads/vlfeat-0.9.20/toolbox/vl_setup')

dataset_path = '/home/void/workspace/birch/workspace/datasets/oxford';
features_size = 364778;

bin_file = strcat(dataset_path,'/feat_oxc1_hesaff_sift.bin');
fid = fopen(bin_file);
data = fread(fid, [128, features_size], '*uint8');

K        = 2 ;
nleaves  = 8 ;
[tree,A] = vl_hikmeans(data,K,nleaves) ;

save('/tmp/test.mat','tree')