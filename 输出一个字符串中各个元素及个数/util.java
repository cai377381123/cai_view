import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Utils {

	public static void getSort(String str) {
		//����API����strת��Ϊchar���͵����顣
		char[] chars = str.toCharArray();
		//����һ��TreeMap���ϣ���������ַ��ʹ����Ķ�Ӧ��ϵ
		TreeMap<Character, Integer> map=new TreeMap<Character, Integer>();
		//ѭ��ȡ�������е�Ԫ��
		for (int i = 0; i < chars.length; i++) {
			//��ȡmap�������ַ����ֵĴ���
			Integer value = map.get(chars[i]);
			if(value==null){
				map.put(chars[i], 1);
			}else{
				value+=1;
				map.put(chars[i], value);
			}
			
		}
		//������ӡ���
		Set<Character> keySet = map.keySet();
		Iterator<Character> it = keySet.iterator();
		while(it.hasNext()){
			Character key = it.next();
			Integer value = map.get(key);
			System.out.print(key);
			System.out.print("("+value+")");
		}
	}

	
}
