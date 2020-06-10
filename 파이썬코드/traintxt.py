import glob
import os.path as path

IMAGE_PATH = 'D:/ARSPA/data/150/ball150_label/'
TEXT_PATH = 'D:/ARSPA/data/150/txtfile/'
file_paths = glob.glob(path.join(IMAGE_PATH, '*.png'))

percent_test=10
file_train=open(TEXT_PATH+'train.txt','w')
file_test=open(TEXT_PATH+'test.txt','w')

cnt=1
index_test=round(100/percent_test)
test_n=0
train_n=0
for path in file_paths:
    if cnt==index_test:
        cnt=1
        file_test.write(path+"\n")
        test_n+=1
    else:
        file_train.write(path+"\n")
        cnt+=1
        train_n+=1
print('test: '+str(test_n)+'  train: '+str(train_n))