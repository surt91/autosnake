# autosnake

This is a toy project to show how to use Python's machine learning ecosystem
on logic implemented in Java classes. In this case to train an autopilot for
the classical game `snake`.

![Trained Snake](img/3000.gif)

More details can be found in the accompanying [blog post](https://blog.codecentric.de/en/2021/11/java-classes-python/).

To try the code yourself, follow the instructions below.

## Usage

```bash
# compile the java classes
# tested with openJDK 11
./mvnw clean package

# install the python dependencies
# (consider doing this in a virtual environment)
pip3 install -r requirements.txt

# train the model in Python using the Java classes
cd py
python3 train_AC.py
# optionally wait and copy the resulting *.h5 files to src/main/java/resources/models
# it might take some time before the target score of 20 is reached
# you might want to change it in line 139 of `py/train_AC.py`
# cp snakeAC.h5 ../src/main/java/resources/models/
cd ..

# run the program using the (pre-)trained model
java -jar target/autosnake-1.0-SNAPSHOT.jar
```
