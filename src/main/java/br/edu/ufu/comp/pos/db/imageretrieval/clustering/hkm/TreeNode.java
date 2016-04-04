package br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import br.edu.ufu.comp.pos.db.imageretrieval.clustering.commons.AbstractTreeNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNode implements AbstractTreeNode {

	private static final EuclideanDistance distance = new EuclideanDistance();
	private double[] centroid;
	private TreeNode[] entries;
	private int insertedNodes = 0;
	private int id;

	public TreeNode(double[] point, int k) {
		this.centroid = point;
		this.entries = new TreeNode[k];
	}

	public void addChild(TreeNode createNode) {
		entries[insertedNodes++] = createNode;

	}

	public boolean isLeaf() {
		return insertedNodes == 0;
	}

	public TreeNode findClosestCluster(double[] sift) {
		if (isLeaf()) {
			return this;
		} else {
			List<TreeNode> elements = Arrays.asList(entries);
			elements.sort((a, b) -> {
				return Double.compare(distance.compute(sift, a.getCentroid()), distance.compute(sift, b.getCentroid()));
			});
			return elements.get(0).findClosestCluster(sift);
		}
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id  = id;
		
	}

}
