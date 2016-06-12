class  selectDemo
{
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");


          int[] arrs={5,2,1,4,6,3};
		  selectSort(arrs);
          
		  System.out.print("[");
		  for (int x=0;x<arrs.length ;x++ )
		  {
               if(x==arrs.length-1)
				   System.out.print(arrs[x]+"]");
			   else
				   System.out.print(arrs[x]+",");
		  }
		
	}
	public static void selectSort(int[] arrs){

        for (int i=0;i< arrs.length-1;i++ )
		{
			for (int j=i+1;j<arrs.length ;j++ )
			{
				int temp=0;
				if(arrs[i]>arrs[j]){
					temp=arrs[i];
					arrs[i]=arrs[j];
					arrs[j]=temp;
				}
			}
		}
	}
}
