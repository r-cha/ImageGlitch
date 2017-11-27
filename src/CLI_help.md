# Command Line Interface
Custom glitches can be crafted using the Command Line Interface currently in development.
Command line arguments must follow strict rules to be successful:
 - The first argument must be the filename of the jpeg to be used as input.
 - The last argument must be the desired filename of the output file.
 - Any intermediate arguments are parsed as manipulations. Any number of manipulations may be completed, and they are executed in order. Only the manipulations listed in CLI_below are supported.
 
### jitterX
Applies a random jitter in the X direction, between 0 and maxDistance. The argument following "jitterX" **must** be an integer. This acts as maxDistance.

### jitterY
Applies a random jitter in the Y direction, between 0 and maxDistance. The argument following "jitterY" **must** be an integer. This acts as maxDistance.