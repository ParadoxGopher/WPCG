package computergraphics.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Gery
 */
public class HalfEdgeTriangleMesh implements ITriangleMesh {
	
	/**
	 * Liste aller Punkte im Mesh
	 */
	private List<Vertex> vertexList = new ArrayList<Vertex>();
	
	/**
	 * Liste aller Dreiecke im Mesh
	 */
	private List<TriangleFacet> facetList = new ArrayList<TriangleFacet>();
	
	/**
	 * Liste aller Halbkanten im Mesh
	 */
	private List<HalfEdge> edgeList = new ArrayList<HalfEdge>();
	
	/**
	 * Map aller ausgehenden Kanten an den Punkten
	 */
	private Map<Vertex, Set<HalfEdge>> edgesOnPoint = new HashMap<Vertex, Set<HalfEdge>>();
	
	private String textureName;
	
	@Override
	public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
		TriangleFacet facet = new TriangleFacet();
		HalfEdge halfEdge1 = new HalfEdge();
		HalfEdge halfEdge2 = new HalfEdge();
		HalfEdge halfEdge3 = new HalfEdge();
		
		halfEdge1.setStartVertex(getVertex(vertexIndex1));
		halfEdge2.setStartVertex(getVertex(vertexIndex2));
		halfEdge3.setStartVertex(getVertex(vertexIndex3));
		
		halfEdge1.setNext(halfEdge2);
		halfEdge2.setNext(halfEdge3);
		halfEdge3.setNext(halfEdge1);
		
		halfEdge1.setOpposite(findOpposite(halfEdge1));
		halfEdge2.setOpposite(findOpposite(halfEdge2));
		halfEdge3.setOpposite(findOpposite(halfEdge3));
		
		getVertex(vertexIndex1).setHalfEgde(halfEdge1);
		getVertex(vertexIndex2).setHalfEgde(halfEdge2);
		getVertex(vertexIndex3).setHalfEgde(halfEdge3);
		
		facet.setHalfEdge(halfEdge1);
		
		//speichern aller objekte
		edgeList.add(halfEdge1);
		edgeList.add(halfEdge2);
		edgeList.add(halfEdge3);
		facetList.add(facet);
		
		//map zum halbkanten finden füllen
		saveEdgesOnPoint(getVertex(vertexIndex1), halfEdge1);
		saveEdgesOnPoint(getVertex(vertexIndex2), halfEdge2);
		saveEdgesOnPoint(getVertex(vertexIndex3), halfEdge3);
		
	}
	
	/**
	 * Dies Methode findet eine Kante von Ziel zu Start der gegebenen Kante falls vorhanden
	 * @param halfEdge die Kante deren gegenueber gesucht wird
	 * @return eine Kante die von Ziel zu Start der gegebenen Kante zeigt oder null
	 */
	private HalfEdge findOpposite(HalfEdge halfEdge) {
		Vertex start = halfEdge.getStartVertex();
		Vertex ziel = halfEdge.getNext().getStartVertex();
		//an Ziel Vertex liegen Kanten an
		if(edgesOnPoint.containsKey(ziel)) {
			//prüfen ob eine der Halbkanten zu Start zeigt
			for(HalfEdge edge : edgesOnPoint.get(ziel)) {
				if(edge.getNext().getStartVertex().equals(start)) {
					return edge;
				}
			}
		}
		return null;
	}
	
	/**
	 * Diese Methode speichert ausgehende Kanten eines Vertex
	 * @param v der Vertex von dem die Kante ausgeht
	 * @param h die ausgehende Kante
	 */
	private void saveEdgesOnPoint(Vertex v, HalfEdge h) {
		//wenn noch keine Kante am Vertex existiert muss der Eintrag erzeugt werden
		if(!edgesOnPoint.containsKey(v)) {
			Set<HalfEdge> newSet = new HashSet<HalfEdge>();
			newSet.add(h);
			edgesOnPoint.put(v, newSet);
		}
		//wenn ein eintrag existiert muss das Set erweitert werden
		else {
			edgesOnPoint.get(v).add(h);
		}
	}
	
	@Override
	public int addVertex(Vertex v) {
		vertexList.add(v);
		return vertexList.indexOf(v);
	}
	
	@Override
	public int getNumberOfTriangles() {
		return facetList.size();
	}
	
	@Override
	public int getNumberOfVertices() {
		return vertexList.size();
	}
	
	@Override
	public Vertex getVertex(int index) {
		return vertexList.get(index);
	}
	
	@Override
	public TriangleFacet getFacet(int facetIndex) {
		return facetList.get(facetIndex);
	}
	
	@Override
	public void clear() {
		vertexList.clear();
		facetList.clear();
		edgeList.clear();
	}
	
	@Override
	public void computeTriangleNormals() {
		
	}
	
	@Override
	public void setTextureFilename(String filename) {
		textureName = filename;
	}
	
	@Override
	public String getTextureFilename() {
		return textureName;
	}
	
}
