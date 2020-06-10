import cv2
import matplotlib.pyplot as plt
import glob
import os.path as path
import numpy as np
import os
IAGE_PATH = 'D:/ARSPA/data/ball30'
file_paths = glob.glob(path.join(IAGE_PATH, '*.png'))
cnt = 0
kernel = np.ones((1, 1), np.uint8)
figure = plt.figure()
for path in file_paths:

    images = cv2.imread(path)
    images_gray=cv2.cvtColor(images,cv2.COLOR_RGB2GRAY)

    th2 = cv2.adaptiveThreshold(images_gray, 255, cv2.ADAPTIVE_THRESH_MEAN_C, \
                                cv2.THRESH_BINARY_INV, 15, 2)
    ct, _ = cv2.findContours(th2, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    if len(ct) != 0:
        #M = cv2.moments(i)
        #if(M['m10']!=0 and M['m01']!=0):
            #cx = int(M['m10'] / M['m00'])
            #cy = int(M['m01'] / M['m00'])
        c = max(ct, key=cv2.contourArea)
        x,y,w,h =cv2.boundingRect(c)
        cv2.rectangle(images,(x,y),(x+w,y+h),(0,255,0),1)
        filename = os.path.basename(path)
        print(filename)
        filepath='D:/ARSPA/data/ball30_label/'
        cv2.imwrite(filepath+filename, images)

    from xml.etree.ElementTree import Element, SubElement, ElementTree

    filename=os.path.basename(path)
    print(filename)
    width = 150
    height = 150
    point1 = (x, y)
    point2 = (x+w,y+h)
    label = 'ball'

    root = Element('annotation')
    SubElement(root, 'folder').text = 'ball150'
    SubElement(root, 'filename').text = filename
    SubElement(root, 'path').text = path
    source = SubElement(root, 'source')
    SubElement(source, 'database').text = 'Unknown'

    size = SubElement(root, 'size')
    SubElement(size, 'width').text = str(width)
    SubElement(size, 'height').text = str(height)
    SubElement(size, 'depth').text = '3'

    SubElement(root, 'segmented').text = '0'

    obj = SubElement(root, 'object')
    SubElement(obj, 'name').text = label
    SubElement(obj, 'pose').text = 'Unspecified'
    SubElement(obj, 'truncated').text = '0'
    SubElement(obj, 'difficult').text = '0'
    bbox = SubElement(obj, 'bndbox')
    SubElement(bbox, 'xmin').text = str(point1[0])
    SubElement(bbox, 'ymin').text = str(point1[1])
    SubElement(bbox, 'xmax').text = str(point2[0])
    SubElement(bbox, 'ymax').text = str(point2[1])

    tree = ElementTree(root)
    tree.write('D:/ARSPA/data/ball150_xml/' + filename + '.xml')
