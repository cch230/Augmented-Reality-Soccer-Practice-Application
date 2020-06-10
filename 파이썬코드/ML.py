from keras.models import Sequential, load_model
from keras.layers import Activation, Dropout, Flatten, Dense, Conv2D, MaxPooling2D
from keras.callbacks import EarlyStopping, TensorBoard, ModelCheckpoint, ReduceLROnPlateau
from keras.regularizers import l2,l1
from sklearn.metrics import accuracy_score, f1_score
import matplotlib.pyplot as plt
import glob
import imageio as imageio
import numpy as np
import os.path as path




IAGE_PATH = 'D:/ARSPA/data/ball90'
file_paths = glob.glob(path.join(IAGE_PATH, '*.png'))

images = [imageio.imread(path) for path in file_paths]
images = np.asarray(images)
image_size = np.asarray([images.shape[1], images.shape[2], images.shape[3]])

images = images / 255
n_images = images.shape[0]
labels = np.zeros(n_images)
for i in range(n_images):
    filename = path.basename(file_paths[i])[0]
    labels[i] = int(filename[0])
del [file_paths]
TRAIN = 0.9
split_index = int(TRAIN * n_images)
shuffled_indices = np.random.permutation(n_images)
train_indices = shuffled_indices[0:split_index]
test_indices = shuffled_indices[split_index:]

x_train = images[train_indices, :, :]
y_train = labels[train_indices]
x_test = images[test_indices, :, :]
y_test = labels[test_indices]
del labels
del test_indices

def visualize_data(positive_images, negative_images):
    figure = plt.figure()
    count = 0

    for i in range(positive_images.shape[0]):
        count += 1
        figure.add_subplot(2, positive_images.shape[0], count)
        plt.imshow(positive_images[i, :, :])
        plt.axis('off')
        plt.title("1")

        figure.add_subplot(1, negative_images.shape[0], count)
        plt.imshow(negative_images[i, :, :])
        plt.axis('off')
        plt.title("0")
    plt.show()


N_TO_VISUALIZE = 10

positive_ex_indices = (y_train == 1)
positive_ex = x_train[positive_ex_indices, :, :]

#positive_ex=positive_ex.astype(np.int32)
positive_ex = positive_ex[0:N_TO_VISUALIZE, :, :]

negative_ex_indices = (y_train == 0)
negative_ex = x_train[negative_ex_indices, :, :]
#negative_ex=positive_ex.astype(np.int32)
negative_ex = negative_ex[0:N_TO_VISUALIZE, :, :]

visualize_data(positive_ex, negative_ex)
N_LAYERS = 4


def cnn(size, n_layers):
    MIN_NEURONS = 20
    MAX_NEURONS = 120
    KERNEL = (3, 3)
    steps = np.floor(MIN_NEURONS / (n_layers + 1))
    nuerons = np.arange(MIN_NEURONS, MAX_NEURONS, steps)
    nuerons = nuerons.astype(np.int64)

    model = Sequential()

    for i in range(0, n_layers):
        print(str(n_layers))
        if i == 0:
            shape = (size[0], size[1], size[2])
            model.add(Conv2D(nuerons[i], KERNEL, input_shape=shape))
        else:
            model.add(Conv2D(nuerons[i], KERNEL))
        model.add(Activation('relu'))
        
    model.add(MaxPooling2D(pool_size=(2, 2)))
    model.add(Dropout(0.3))
    
    model.add(Conv2D(64, (3,3), activation="relu"))
    model.add(MaxPooling2D(pool_size=(2,2)))
    model.add(Dropout(0.25))
    
    model.add(Conv2D(64, (3,3), activation="relu"))
    model.add(MaxPooling2D(pool_size=(2,2)))
    model.add(Dropout(0.25))
    
    model.add(Flatten())
    model.add(Dense(MAX_NEURONS, kernel_regularizer=l1(0.0001)))
    model.add(Activation('relu'))
    model.add(Dropout(0.4))
    model.add(Dense(1,kernel_regularizer=l2(0.0001)))
    model.add(Activation('sigmoid'))
    
    model.compile(loss='binary_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    print(str(i))
    model.summary()
    print("finish")
    return model


print(str(image_size))

model = cnn(size=image_size, n_layers=N_LAYERS)

EPOCHS = 20
BATCH_SIZE = 600

PATIENCE = 10

print("정확도 : %.4f" % (model.evaluate(x_test, y_test)[1]))

callbacks_list = [
    EarlyStopping(
        monitor='loss',
        min_delta=0,
        patience=PATIENCE,
        verbose=0,
        mode='auto',
    ),
    TensorBoard(
        log_dir='logs',
        histogram_freq=1,
        write_graph=True,
        write_images=True,
    ),
    ModelCheckpoint(
        filepath='ball_image_classification_model.h5',
        monitor='val_loss',
        save_best_only=True,
    ),
    ReduceLROnPlateau(
        monitor='val_loss',
        factor=0.1,
        patience=10,
    )
]
hist = model.fit(x_train, y_train, epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks=callbacks_list,
                 validation_data=(x_test, y_test), validation_split=0.15)
model.save('ball_image_classification_model_test.h5')
print("saved model.h5")
model_json=model.to_json()
with open("model_json.json","w")as json_file:
    json_file.write(model_json)
print("saved model_json.json")

test__predictions = model.predict(x_test)
test__predictions = np.round(test__predictions)

accuracy = accuracy_score(y_test, test__predictions)
print("Accuracy : " + str(accuracy))



model = load_model('ball_image_classification_model_test.h5')
print("Loaded model")
fig, loss_ax = plt.subplots()

acc_ax = loss_ax.twinx()

loss_ax.plot(hist.history['loss'], 'y', label='train loss')
loss_ax.plot(hist.history['val_loss'], 'r', label='val loss')

acc_ax.plot(hist.history['accuracy'], 'b', label='train acc')
acc_ax.plot(hist.history['val_accuracy'], 'g', label='val acc')

loss_ax.set_xlabel('epoch')
loss_ax.set_ylabel('loss')
acc_ax.set_ylabel('accuray')

loss_ax.legend(loc='upper left')
acc_ax.legend(loc='lower left')

plt.show()

import numpy as np
from keras.preprocessing import image
imagename='Baseball.png'
test_image= image.load_img('C:/Users/cch/PycharmProjects/untitled1/venv/Scripts/'+imagename,target_size=(30,30))
test_image= image.img_to_array(test_image)
test_image= np.expand_dims(test_image,axis=0)
model = load_model('ball_image_classification_model_test.h5')
result= model.predict(test_image)
np.set_printoptions(formatter={'float': lambda x: "{0:0.4f}".format(x)})

for i in result:
    if i>=0.8:
        print(i)
        print(imagename)
        print("ball")
    else:
        print(i)
        print("No")
        print(imagename)
