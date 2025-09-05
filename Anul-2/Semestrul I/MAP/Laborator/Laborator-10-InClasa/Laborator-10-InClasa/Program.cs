// See https://aka.ms/new-console-template for more information

using Laborator_10_InClasa;

Person person=new Person(10);
person.Greet();

Student student=new Student(21);
student.Greet();
student.ShowAge();

Profesor profesor=new Profesor(30,"Astazi predam matematica");
profesor.Explain();