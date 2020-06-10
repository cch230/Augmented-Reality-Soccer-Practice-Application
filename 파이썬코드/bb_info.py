import xml.etree.ElementTree as Et
import glob
import os.path as path

xml_path = 'D:/ARSPA/data/150/ball150_xml/'
TEXT_PATH = 'D:/ARSPA/data/150/ball150/'
file_paths = glob.glob(path.join(xml_path, '*.xml'))
cnt = 1
print("XML parsing START")
for paths in file_paths:
    print(cnt)
    filename = path.basename(paths)
    arr=filename.split('.')
    bb_info = open(TEXT_PATH + arr[0]+'.txt', 'w')
    bb_info.write(arr[0]+"\n")
    xml = open(paths, "r")
    tree = Et.parse(xml)
    root = tree.getroot()

    objects = root.findall("object")

    for _object in objects:
        bndbox = _object.find("bndbox")
        xmin = bndbox.find("xmin").text
        ymin = bndbox.find("ymin").text
        xmax = bndbox.find("xmax").text
        ymax = bndbox.find("ymax").text
        center_x = int(xmin) + int(xmax) / 2
        center_y = int(ymin) + int(ymax) / 2
        width = int(xmax) - int(xmin)
        height = int(ymax) - int(ymin)
        bb_info.write(
            "{0} {1} {2} {3} {4}\n".format(str(center_x), str(center_y), str(width), str(height), "ball"))
        cnt += 1

print("XML parsing END")
