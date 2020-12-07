using System;
using System.Collections.Generic;

namespace lab4
{
    class Program
    {

        public static List<String> links = new List<String>();

        public static void Main(string[] args)
        {

            links = new List<String> {"emag.ro", "bucataras.ro/retete","olx.ro"};
            WithCallbacksOnly.run(links);
            //WithTasks.run(links);
            //WithAsyncTasks.run(links);
        }
    }
    
}