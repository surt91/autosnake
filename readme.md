# Autosnake

This is a toy project to show how to use Python's machine learning ecosystem
on logic implemented in Java classes. In this case to train an autopilot for
the classical game `snake`.

![Trained Snake](img/3000.gif)

More details can be found in the accompanying blog post, which will follow soon.

To try the code yourself, follow the instructions below.

## Usage

```bash
# compile the java classes
# tested with openJDK 11
./mvnw clean package

# install the python (do this in a virtual environment, if you prefer)
pip install -r requirements.txt

# train the model in Python using the Java classes
cd py
python3 train_AC.py
# optionally copy one of the resulting *.h5 files to src/main/java/resources/models
# cp snakeAC.h5 ../src/main/java/resources/models/

# run the program using the (pre-)trained model
cd ..
java -jar target/autosnake-1.0-SNAPSHOT.jar
```
