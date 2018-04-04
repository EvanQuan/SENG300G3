SENG 300 Group 2 Iteration 3
--------------------------

[Slack Channel link](https://seng300w2018g2i3.slack.com/)

[System Spreadsheet](https://docs.google.com/spreadsheets/d/1-xapWVodk4DW2dW3CrfHNiGbSCNzF-H4mEmOvLLXMmg/edit?usp=sharing)

[Report](https://docs.google.com/document/d/1QYiC9THKIPZ7UjDQpc4Mw9hxBc-GlB3OcXumWlKw9Eo/edit)

Table of Contents
-----------------
1. [Getting Started](#getting-started)
3. [Resources](#resources)
4. [Stuff to Keep Everyone Sane](#stuff-to-keep-everyone-sane)
    - [Upload files button](#be-wary-of-the-upload-files-button-on-github)
    - [Check before pushing](#check-before-pushing)
5. [Contact](#contact)

The Real Git Flow
-------------------------
<img src="https://i.redd.it/dsdbmyoirso01.jpg">


Getting Started
---------------

#### .classpath
After pulling, create a `.classpath` file specific to your machine (currently ignored in `.gitignore` to prevent conflicts between machines).

If using the Linux computers at the university, copy and paste this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8"/>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/4"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.core.jobs_3.9.0.v20170322-0013.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.equinox.common_3.9.0.v20170207-1454.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.core.resources_3.12.0.v20170417-1558.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.osgi_3.12.0.v20170512-1932.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.core.contenttype_3.6.0.v20170207-1037.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.equinox.preferences_3.7.0.v20170126-2132.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.jdt.core_3.13.0.v20170516-1929.jar"/>
	<classpathentry kind="lib" path="/opt/eclipse-java/plugins/org.eclipse.core.runtime_3.13.0.v20170207-1030.jar"/>
	<classpathentry kind="output" path="bin"/>
</classpath>
```

Else, copy and paste this and manually reference the external jars in Eclipse :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8"/>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.junit.JUNIT_CONTAINER/4"/>
	<classpathentry kind="output" path="bin"/>
</classpath>
```

Or, just copy the same `.classpath` file from your previous iteration.

Resources
---------

- [ASTParser Javadoc](https://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FASTParser.html)
- [Diagram Drawing Tool](https://draw.io)
	- Exporting diagrams as `.pdf` and save the `.xml` file in case of future edits.


Stuff to Keep Everyone Sane
--------------

Most of this is common sense, but in a large group such as this, there can be a lot of unnecessary chaos that can be avoided. Understandably, no one likes be told what to do, but here are some things to consider to ensure we'll all be one big happy family:

#### Be wary of the "Upload files" button on Github
The "upload files" button on the github page can be pretty convenient for quickly adding new things or making changes. However, it can also cause some problems:
##### 1. It can bypass the .gitignore
Certain files, like `.classpath` or the `/bin/` directory as specific to each machine, and so should be ignored by git. Uploading your versions of these files through the Upload files button bypasses the `.gitignore`, which will cause everyone else to now track those files. Having other people pull your versions of those files onto their machines will screw up their eclipse workspaces.

##### 2. It can mess up other people's work

Generally, if you are behind the master branch and want to push some local changes, you would normally have to pull those changes before you can push your own. If you ignore all that and directly upload your files without pulling, you can override other people's work or cause merge conflicts that could have been avoided had you pulled beforehand.

In conclusion, feel free to continue using the "Upload files" button, just be aware of what you are doing.

#### Check before pushing

If you working on the code, here are some things to look over before pushing to master.

##### 1. Make sure the code actually compiles
This should seem obvious to most people, but this is a surprisingly common problem that occurs. If you're using Eclipse, take advantage of Eclipse's linter and fix all the compile-time errors it catches. If you are using some other editor that has no linting capabilities or very simple linting, check back on Eclipse and see what errors it catches just to double check.

##### 2. Run the TestSuite often
Often when you are implementing a new feature or fixing a bug, you will break other parts of the code in unforseen ways. It's a good habit to run the TestSuite every once in a while so you know when ya dun goofed. If the code you are pushing is failing more test cases than it did before you made your changes, that's probably a sign to wait and fix those bugs first.

Contact
------------

[Evan Quan](https://github.com/EvanQuan) - 10154242 - evan.quan@ucalgary.ca

[Mona Agh](https://github.com/Monaagh) - 30033301 - mona.agh1@ucalgary.ca

[Marcello Di Benedetto](https://github.com/Marcellod1) - 30031839 - marcello.dibenede1@ucalgary.ca

[John Benedict Mendoza](https://github.com/JBenedictM) - 30028470 - johnbenedict.mendoza@ucalgary.ca

[Matthew Buhler](https://github.com/Eventrope) - 00332036 - matthew.buhler@ucalgary.ca

[Tyler Chow](https://github.com/TylerChow) - 30024575 - tyler.chow@ucalgary.ca

[Dominic Demierre](https://github.com/dominic-demierre) - 30029784 - dominic.demierre@ucalgary.ca

[Philip Dometita](https://github.com/philipdometita) - 30032976 - philip.dometita@ucalgary.ca

[Osagie Omigie](https://github.com/osagieomigie) - 30008204 - osagie.omigie@ucalgary.ca

[Zheng Yang Toh](https://github.com/zytoh) - 10129855 - zytoh@ucalgary.ca
