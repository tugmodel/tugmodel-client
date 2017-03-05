# tugmodel-client

#What it is?:

Tugmodel is a tugged(active) POJO framework, metadata framework, domain model keeper, service orchestrator, SQL/NoSQL ORM, distributed service framework, **do it yourself framework**. 

It has 3 concepts: **TUGS, MODELS and METADATA**.
  1. **A tug** can be a task, workflow, comunication mean, storage mean, processing mean, etc. Basically it is a service abstraction interface used to **drive(tug)**, transport or process the models. Inspired by BPMN activities.
  2. **A model** is an **active POJO** that stores the data processed by the tugs. 
  3. **A metadata** is also a model but it stores descriptive, structural and administrative information about a model that keeps real data.
You can/should **plug in your own tugs, your own models and your own metadata**. Default implementations are provided mainly for reference and basic functionality.

#Use cases:
- for abstracting the communication/processing layer(active record pattern - by Martin Fowler):
      `model.save()` instead of `MySaveService.save(model)`.
- for allowing an **EXTENSIBLE model** (`model.set(key, value)`) where **unexpected** attributes can be stored by the model . 
- for user friendly and implementation agnostic APIs.
- for having the GUI derive the domain model metadata from backend metadata. **E.g.: if a model attribute maxLength restriction changes then that will automatically be enforced in the UI**.
- for storing models in **database, files, memory**. Default implementations available for all three.
- for storing models in SQL db an **ORM like tug** is available:
```java
      List<Employee> list = Employee.s.where("country=US&status=active").limit(40).offset(20).orderBy("name asc");
      Employee e = list.get(0);
      e.delete();
      Employee emp = new Employee().setId("user1234").fetch();
      emp.setName("John Doe");
      emp.set("extraField", "extra info");
      emp.save();      
 ```
- for decoupling code into processing units and combining those units easily using workflows and proxy tugs.
- for service orchestration and code decoupling using linear workflow (SimpleWorkflowTug). No BPMN support yet. 
- for calling operations(tugs) remotely like they would be run locally. 
- for multitenancy
- for chaining calls: new Model().set("attr", 1).save();
- for configuration using JSON files (you can implement also non JSON Config models)
- for when system configuration is also a model that can be fetched or updated(config.setMyConfig("X").update()).
- for removing the need for SQL seeds and instead promotes JSON seeds.
- for internal product(same process or not) communication using POJOs(the models) and communication tugs. For multi process communication the models and communication tugs should to be bundled together in an `interfaces_common.jar` that is accessible by both processes.
- for external product communication like for example [REST](https://martinfowler.com/articles/richardsonMaturityModel.html), a model can be easily constructed from a REST request:
```java
   // GET /slots/1234
      Slot slot = new Slot().setId("1234").fetch();
      
   // GET /slots?date=20100104&status=open
      List<Slot> slots = Slot.s.where("date=20100104&status=open");
      return slots;
 ```  
- for code interpretation/generation based on metadata. We favor code interpretation over code generation.
- for your own custom metadata. Default implementation is also available for extension.

**Demo**
Hello world demo using a User model.
1. Create model. Model classes can also be generated from the provided metadata as long as the metadata follows the general format.
	NOTE: You need to add a static field that refers to the tug behind the class.
	      The alternative to this would be an instrumentation maven plugin that would add the static field/method automatically (using javassist). 
2. Select/configure a default tug for your model.
3. Define metadata. 
This is all, now you can tug the model.

**NOTES**
MyClass.s is just a shortcut to a Tug. You can name it whatever. Although it could be generated it is more clear to set and name-it yourself. 
"s" is a good choice because plural subjects end with "s". But also you can use the tug directly if you want obtaining it from the TugFactory or a specific tug instance.   

Features:

All the operations(including business) are started from a model. E.g.: model.save() but a tug is the one executing the operation.
Tugs are data processing elements. One of the central tugs is the worflow tugs. Other tugs are simple like for network communication tug, SQL tug, NoSQL tug, etc. The framework provides a basic set of tugs but you can add your own very easy.
The metadata is necessary for allowing a **tug deliver the model** at the destination.
   Also based on the metadata you can generate the code for the models(javassist and freemaker).
   JSON is used as default for metadata and serialization format but XML can used also by implementing a new IMapper(using JIBX for XML serialization).
   A Netty tug will be available for tugging models over the network.   



**Dependencies**


Obs:
  1. [Direct operations](#direct-operations) ([active record pattern](https://en.wikipedia.org/wiki/Active_record_pattern)).
  2. Pluggable network communication tugs and easy configuration of workflow tugs.
  3. Simple [workflow or tug](#workflow tug) for decoupling.
  4. [No more SQL seeding](#no-more-sql-seeding-scripts). Use JSON for seeds.
  5. Metadata based models. Metadata generated models(not recommended).
  6. SQL tug with plain JDBC. 
  7. Network communication tug using   
  
Similar patterns in: Ruby ActiveModel, JavaLite and Backbone.js.        


#Direct operations
 ```java
 //MyControlService.save(myObj);
 myObj.save();
 ```
Instead of doing `MyControlService.save(myObj)` you will do **`myObj.save()`**.
The benefit is that it allows an abstraction of the operation and transport(e.g. the destination could be a local/remote service or a rest/non rest provider depending on the configured proxy). This is inspired by Martin Fowler's [active record pattern](https://en.wikipedia.org/wiki/Active_record_pattern) and backbonejs.

#Workflow tug
Allow simple workflows (called `tugs`). A `tug` is made of tasks. This encourages decoupling each `model/record` action into basic units(tasks) of work, e.g.:
  1. `authentication`
  2. `authorization`
  3. `normalization`
  4. `cache retrieval if available`
  5. `action performing`
  6. `auditing`
  7. `cache updating`

Tug configuration is done in `**tugs.json**` file. 
Each tug starts with a coordinator task. The coordinator is responsible for parsing the workflow configuration, executing the tug tasks, listening for events from the tasks that it runs, etc.
Since service transactions are hard to achieve when having a tug made of external calls to services a compensation mechanism will be added. In case of an error all the registered compensation tasks will be executed in the reverse order. For example the compensation task of `obj.create` is `obj.delete`.
The default tug coordinator task will also be able to interrupt the flow as a response to an `INTERRUPT` event that a task could fire. 
  
#No more SQL seeding scripts
 We all know that at installation seeding scripts are run first. This has several disadvantages:
  * It takes time to write and maintain SQL scripts for your databases.
  * You need to have DB knowledge of the db structure before you do any modifications.
  * Sometimes **your SQL becomes your API** because it's easier writing SQL than doing the right thing: an API.

The solution is to add an `install tug`. Use JSON seeds as input to the `install tug`. Beside the elimination of SQL scripts this has the benefit that it also tests your tugs. Still SQL's will remain for database & tables creation and possibly for static data but all these should become a `tug task`.
A JSON operation marking scheme is used here and could be similar to JSON-PATCH or [JSON-API](http://jsonapi.org/).

##Other ideas:
  * TODO: add ModelException and TugException classes. 
  * **GUI web app** for `tug` (simple workflow) configuration and stats reports.  
  * [JSON-API](http://jsonapi.org/) proxy implementation.
  * Integrate a [free](https://github.com/Activiti) or develop a complex workflow [BPMN](https://en.wikipedia.org/wiki/Business_Process_Model_and_Notation) as alternative to `tugmodel` simple workflow. 
  * Workflow change detection.  
  * Tug task coordinator needs capability querying(`isAbleFor(feature/task)`) so that it does not execute tasks that it can not coordinate.
  * Scriptable installation [CLI](https://en.wikipedia.org/wiki/Command-line_interface) called "tug" callable from product installler (for JSON seeds), updates or new packages. Follows Martin Fowler's [microservices](https://martinfowler.com/articles/microservices.html) deployment model.
  * Rest level 2,3 proxy.

# License #
`tugmodel` is licensed under the [Apache License 2.0] (https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)). Click [link] (https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)) for more details.
For additional/alternative licensing questions, please contact Cristian Donoiu.
    