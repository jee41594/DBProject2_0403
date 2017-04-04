/*
��ü���� ����� �ڹٿ����� ������ �繰�� Ŭ������ ����������
Database������ ������ �繰�� Entity��� ��ü �������� ǥ���Ѵ�.
�ᱹ ��ü�� ǥ���ϴ� ����� �ٸ� �� ������ ����.
���� �ݿ��̶�� ������ ����!

��ü���� ���� Ŭ������ �ν��Ͻ��� �����س��� ��Ǫ���̶��
�����ͺ��̽� �о߿��� ���̺��� ���ڵ带 ������ �� �ִ� Ʋ�� ���� �����ϴ�.
�̶� �ϳ��� ���ڵ�� �ᱹ �ϳ��� ��ü�� ���� �Ѵ�.
���) ���̺� �����ϴ� ��ǰ ���ڵ� ���� �� 5�� ��� �����ڴ� �� ������ ���ڵ带
5���� �ν��Ͻ��� ���� ������ �ȴ�.

�Ʒ��� Ŭ������ ���� �ۼ����� �ƴ϶� �� �Ѱ��� ���ڵ带 ��� ���� ��������뵵�θ�
����� Ŭ�����̴�. ���ø����̼� ����Ӿ߿����� �̷��� ������ Ŭ������ ������
VO, DTO�� �Ѵ�.
VO = Value Object = ���� ��� ��ü
DTO = Data Transfer Object = ���� �����ϱ� ���� ��ü

 */

package book;
//�������� ���̺�� ��ġ���Ѿ� �Ѵ�.
public class SubCategory {
	/*
	�� private? ������ �ƴ϶� �����͸��� �ݴ� �뵵�̱� ������ �����ʹ� ��ȣ�Ǿ�� �Ѵ�.
	DummyŬ����
	���� ������ ��� �׸���!
	
	�迭���� ��������?
	�迭�� ��ü�� ������ Ʋ�� ������� ���ߵǹǷ� 
	��ü�� ó���ϴ� ���� �ξ� �� �۾�����̳� ���� �� ����!
	�迭�� String�̳� int�� ���ϱ�� ������ �ڷ������� ���� �� ����
	��ü���������� �ʴ�.
	*/
	
	private int subcategory_id;
	private int topcateogry_id;
	private String category_name;
	
	//�̰� ������ ������ �� �ֵ��� ����ȭ ��Ű��!!
	//alt+shift+s -> generate getter/setter

	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public int getTopcateogry_id() {
		return topcateogry_id;
	}
	public void setTopcateogry_id(int topcateogry_id) {
		this.topcateogry_id = topcateogry_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
}
