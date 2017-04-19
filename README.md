# ImageGlitch
A library of methods for "glitching" images

Consists of ImageManip and ImageGlitcher.
ImageManip is the main driver, but all glitching functionality is available via the methods of an ImageGlitcher object.

The idea is to use ImageManip simply to chain together the functionality of the ImageGlitcher in order to create cool things.
All manips must start with a call to load() and end with save() in order to save the resulting manipulation.

The ImageManip class ususally contains whatever I am testing at the time. Below is a good "first time" test for images greater than 1500x1500.

```java
String inName = "in.jpg";
String outName = "out.jpg";
BufferedImage in = null;

ImageGlitcher glitch = new ImageGlitcher();		

// Import image
System.err.println("Reading image...");
in = glitch.load(in, inName);

// Generate derivative arrays
System.err.println("Generating arrays...");
int[][] rgb = glitch.makeRGB(in);
int[][] lum = glitch.makeLum(rgb);

// Sort arrays
System.err.println("Sorting...");
glitch.dependentQuicksort(rgb, lum);
glitch.rewriteImg(in, rgb);

// Block glitch image
System.err.println("Block glitching...");
for (int i = 500; i <= 1500; i += 500) {
  glitch.blockGlitch(rgb, i, i);
}
glitch.rewriteImg(in, rgb);

// Save manipulated image
System.err.println("Saving final image...");
glitch.save(in, outName);

System.err.println("Done!");
```
