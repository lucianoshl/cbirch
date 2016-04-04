package br.edu.ufu.comp.pos.db.imageretrieval.clustering.hkm;

import java.util.ArrayList;
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
	private List<TreeNode> entries;
	private int id;

	public TreeNode(double[] point, int k) {
		this.centroid = point;
		this.entries = new ArrayList<TreeNode>();
	}

	public void addChild(TreeNode createNode) {
		entries.add(createNode);

	}

	public boolean isLeaf() {
		return entries.isEmpty();
	}

	public TreeNode findClosestCluster(double[] sift) {
		if (isLeaf()) {
			return this;
		} else {
			entries.sort((a, b) -> {
				return Double.compare(distance.compute(sift, a.getCentroid()), distance.compute(sift, b.getCentroid()));
			});
			return entries.get(0).findClosestCluster(sift);
		}
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

	}

}
