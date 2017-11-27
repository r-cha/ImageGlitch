# ImageGlitch
A library of methods for "glitching" images

## Using
### Quick and Dirty
Download the content of the src directory to the location of your choice.
Compile the contents of src by navigating to their directory and type `javac [filename1] [filename2] ... [filenamen]` where in this case, the filenames are ImageManip.java, ImageGlitcher.java, and ResourceGenerator.java. (Requires a JDK)
After this has completed, there will be three new .class files in the directory.
Add the jpeg image of your choosing to the same directory.
Type `java ImageManip` into the command line to run the demo.
### Custom glitches
Custom glitches can be crafted using the Command Line Interface currently in development.
Command line arguments must follow strict rules to be successful:
 - The first argument must be the filename of the jpeg to be used as input.
 - The last argument must be the desired filename of the output file.
 - Any intermediate arguments are parsed as manipulations. Any number of manipulations may be completed, and they are executed in order. Only the manipulations listed in CLI_help.md are supported.

## Examples
The ImageManip class usually contains whatever I am testing at the time. There are no guarantees its default results will be even remotely interesting.

To see a collection of samples of the possibilities of this program, see the Imgur album below.
http://imgur.com/a/r8zaF
