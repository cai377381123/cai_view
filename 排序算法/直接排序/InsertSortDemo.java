class InsertSortDemo 
{
	public static void main(String[] args) 
	{
		
          int[] arrs={5,4,9,6,7,8,2};
		  insertSort(arrs);
		 printArray(arrs);
	}

	public static void insertSort(int[] arrs){

        for (int i=1;i<arrs.length ;i++ )
		{
			//定义一个临时存储变量
			int temp=arrs[i];
            
			int j = i;
			//????
			while(j>0 && temp<arrs[j-1]){
                //将满足条件的前一个值赋值给当前值
                arrs[j]=arrs[j-1];
                j--;
			}
			arrs[j]=temp;
		}
	}

	public static void printArray(int[] arrs){

         System.out.print("[");
		  for (int x=0;x<arrs.length ;x++ )
		  {
               if(x==arrs.length-1)
				   System.out.print(arrs[x]+"]");
			   else
				   System.out.print(arrs[x]+",");
		  }
	}
}
