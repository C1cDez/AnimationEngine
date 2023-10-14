# AnimationEngine
Java Engine for coding Animations

**VideoIterator** - frames iterator

Animation Code Examples
---
Creates video iterator:
```
VideoIterator iterator = new VideoIterator(index -> new Frame(graphics -> {
	...//Draw graphics
}));
```
Example (rotating point):
```
VideoIterator iterator = new VideoIterator(index -> new Frame(graphics2D -> {  
	double theta = (double) index * index / 180;  
	double radius = theta * 0.2;  
	double dx = radius * Math.cos(theta), dy = radius * Math.sin(theta);  
	int x = (int) (dx * 300 + width / 2), y = (int) (dy * 300 + height / 2);  
	graphics2D.setColor(Color.RED);  
	graphics2D.fillOval(x - 5, y - 5, 10, 10);  
}));
```
Exporting Video
---
Export:
```
VideoExport.export(iterator, new VideoExport.ExportSettings(
	width, 
	height, 
	fps, 
	totalFrames, 
	"Frames Destination Folder", 
	"Video Output File", 
	removeFrames
));
```
Example (Full HD, 60 fps, 3 seconds):
```
VideoExport.export(iterator, new VideoExport.ExportSettings(1920, 1080, 60, 180, "C:/Output/frames", "C:/Output/video.mp4", true));
```
Thanks for Downloading
---
Supplement the engine with new features, please!
:)
