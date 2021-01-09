import eu.mihosoft.vrl.v3d.svg.*;

import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Extrude;
import eu.mihosoft.vrl.v3d.Polygon

File f = ScriptingEngine
	.fileFromGit(
		"https://github.com/bcline46/BensSVGs.git",//git repo URL
		"master",//branch
		"bensquare.svg"// File from within the Git repo
	)
println "Extruding SVG "+f.getAbsolutePath()
SVGLoad s = new SVGLoad(f.toURI())
println "Layers= "+s.getLayers()
// A map of layers to polygons
HashMap<String,List<Polygon>> polygonsByLayer = s.toPolygons()
// extrude all layers to a map to 10mm thick
HashMap<String,ArrayList<CSG>> csgByLayers = s.extrudeLayers(10)
// extrude just one layer to 10mm
def holeParts = s.extrudeLayerToCSG(10,"1-holeslayer")
// seperate holes and outsides using layers to differentiate
def outsideParts = s.extrudeLayerToCSG(10,"2letterslayer")
					.difference(holeParts)
// layers can be extruded at different depths					
def boarderParts = s.extrudeLayerToCSG(5,"3-bottomlayer")

return [CSG.unionAll([boarderParts,outsideParts])]