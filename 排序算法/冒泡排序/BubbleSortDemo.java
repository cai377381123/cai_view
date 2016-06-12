class BubbleSortDemo 
{
	public static void main(String[] args) 
	{
		//ц╟ещеепР
         int[] arrs={5,2,1,6,4,8,9};
		 bubbleSort(arrs);
         printArray(arrs);

	}

	public static void bubbleSort(int[] arrs){

		for (int i = 0;i < arrs.length-1;i++ )
		{
			for (int j=0;j<arrs.length-i-1 ;j++ )
			{
				if(arrs[j]>arrs[j+1]){

                     int temp=0;
					 temp=arrs[j];
					 arrs[j]=arrs[j+1];
					 arrs[j+1]=temp;
				}
			}
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
