Pos Tagging [<img src=video1.jpg width="5%">](https://youtu.be/gQmc7Nhwhuk)[<img src=video2.jpg width="5%">](https://youtu.be/GHUib73MRks)
============

This is a tool meant for tagging words with their part-of-speech, a grammatical category based on their function within a sentence, such as noun, adjective, verb, and so forth. 

For Developers
============

You can also see [Python](https://github.com/starlangsoftware/EnglishPosTagger-Py), [Cython](https://github.com/starlangsoftware/EnglishPosTagger-Cy), [C++](https://github.com/starlangsoftware/EnglishPosTagger-CPP), [Swift](https://github.com/starlangsoftware/EnglishPosTagger-Swift), [Js](https://github.com/starlangsoftware/EnglishPosTagger-Js), or [C#](https://github.com/starlangsoftware/EnglishPosTagger-CS) repository.

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called PosTagger will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/PosTagger.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `PosTagger/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run PosTagger.

**From Console**

Go to `PosTagger` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `PosTagger` root module.

**From Console**

Use below line to generate jar file:

     mvn install

## Maven Usage

        <dependency>
            <groupId>io.github.starlangsoftware</groupId>
            <artifactId>PosTagger</artifactId>
            <version>1.0.0</version>
        </dependency>

Detailed Description
============

+ [PosTagger](#postagger)

## PosTagger

To train the PosTagger which is used for English pos tagging 

	void train(PosTaggedCorpus corpus)
	
To save the trained PosTagger model

	void saveModel()
	
To load an already trained PosTagger model

	void loadModel()
	
To tag a sentence, using a newly trained or loaded PosTagger model

	Sentence posTag(Sentence sentence)
	

3 different PosTagger models are supported:
The one that is used to tag the sentences with a random tag

	DummyPosTagger
	
the one that tags the word with the most used tag for a given word 

	NaivePosTagger
	
the one that does an Hmm based training and tags the words accordingly 

	HmmPosTagger
