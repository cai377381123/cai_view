class halfSerachDemo 
{
	public static void main(String[] args) 
	{
		
          
         int[] arrs={1,3,5,6,8,9,17};
		 System.out.println(halfSearch(arrs,20));
	}

	public static int halfSearch(int[] arrs,int key){

         int min=0;
		 int max=arrs.length-1;
		 int mid=(min+max)/2;

		 while(arrs[mid]!=key){

               if(arrs[mid]>key)
				   max=mid-1;
			   else if(arrs[mid]<key)
				   min=mid+1;
                
                 if(min>max)
					 return -1;

              mid=(min+max)/2;


		 }
         return mid;
	}

	public static int halfSearch2(int[] arrs,int key){

         int min = 0;
		 int max = arrs.length-1;
		 int mid;
          //只要数组里有值，就可以进行二分法查找
		 while(min<=max){

               mid=(min+max) >> 1;
              if (arrs[mid]>key)
              {
				  max=mid-1;
              }
			  else if(arrs[mid]<key)
				  min=mid+1;
              else
				  return mid;

		 }

		 return -1;
	}


}
