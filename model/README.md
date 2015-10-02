1. Download and install the modeling edition of eclipse
    - https://eclipse.org/downloads/packages/eclipse-modeling-tools/lunasr2
2. Upon first starting eclipse, goto the help menu, and select “Install Modeling Components”, check Papyrus and hit “Finish”
    - Papyrus will download and install, and restart eclipse
3. After the Papyrus download, goto the help Menu and select “Install Papyrus Additional Components”, and select “”Diagram Generation” and “Papyrus Layers” and hit Finish
    - These plugins will be installed, then restart eclipse
4. After these components are installed, create a Papyrus perspective by clicking on the “+” sigh in the perspective bar near the upper right (there will already be a Java Perspective created”)
    - Select “Papyrus” and hit “OK”
5. Assuming that your eclipse working is ~/workspace:
    - make a copy of the the folder “cl-mef-uml”, and (it contents) in ~/workspace (so that you end up with a ~/workspace/cl-mef_uml” directory”
6. Activate the Papyrus perspective in eclipse, and goto the menu “File/New/Papyrus Project”
    - give the new project the name “cl-mef-uml” and the files that you placed into your workspace directory will automatically be imported into the new project.
7. In the “Model Explorer” pane on the left, expand the “>Model” tree to see all of the the model components
8. To view the UML class digram click on “Show Diagrams” icon withing In the “Model Explorer” pane on the left, and select the "MEF Classes" diagram.
