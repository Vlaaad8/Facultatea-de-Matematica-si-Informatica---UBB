// See https://aka.ms/new-console-template for more information

using Laborator_12_InClsa;

List<Dog> dogs = new List<Dog>();
Huski h=new Huski("max",10,100);
Huski h1=new Huski("maximus",2,3);
Labrador l=new Labrador("Ben",2,"Maro");
Labrador l1=new Labrador("Luna",5,"Negru");
Dog dog=new Dog("Rex",6);
dogs.Add(h);
dogs.Add(h1);
dogs.Add(l);
dogs.Add(l1);
dogs.Add(dog);
dogs.ForEach(dog=>dog.Eat());
Console.WriteLine("Exercitiul 3:");
for (int i = 0; i < dogs.Count; i++)
{
    if (dogs[i] is Huski)
    {
        dogs[i].Eat();
    }
}

Dictionary<int,string> dict = new Dictionary<int, string>();
for (int m = 0; m< 5; m++)
{
    dict.Add(m,"Strada Pacii" +m);
}
foreach (var keyValuePair in dict)
{
    Console.WriteLine(keyValuePair);
}
