package comum.json.comparator;

import java.util.Comparator;

import comum.json.artefatos.JElement;

public class IntegerComparator implements Comparator<JElement>, ComparatorInterface {
	private String orderBy = "";
	private OrderType orderType;

	public IntegerComparator(String orderBy, OrderType orderType) {
		this.orderBy = orderBy;
		this.orderType = orderType;
	}

	@Override
	public int compare(JElement arg0, JElement arg1) {
		return arg0.asObject().integer(orderBy).compareTo(arg1.asObject().integer(orderBy)) * (orderType == OrderType.ASC ? 1 : -1);
	}
}
