# tugmodel-client

#What it is?:

Tugmodel is a full framework made of three concepts:
###1. **Tugs**.
###2. **Models**.   
###3. **Metadata**.
**A tug** can be a workflow or a work horse that drives and processes your data models. 
**A model** is an **active POJO** that stores the data and is delivered & processed by tugs. 
**A metadata** is also a model but it stores descriptive, structural and administrative information about a model that keeps real data.
Default implementations for all 3 concepts are available but you can also easily plug in **your own tugs, your own models and your own metadata**.
The model data can be serialized in any format as long as a corresponding Mapper exists. 

#What is for?:
- for binding specific data(models) to specific processes(tugs)
- for user friendly APIs (see Joshua Bloch).
- for implementation agnostic APIs
- for having the UI derive the metadata from backend metadata.
- for storing models in database, disc, memory. Default implementations available for all three.
- for chaining calls: new TModel().set("f", 1).save();
- for decoupling code into processing units and combining those units easily
- for service orchestration and code decoupling using linear workflow (SimpleWorkflowTug). No BPMN support yet. 
- for calling operations(tugs) remotely like they would be run locally 
- for multitenancy
- for promoting configuration using JSON files (you can implement custom <Format>ComfigTug for specific file format)
- for removing the need for SQL seeds and instead promotes JSON seeds.
- for when system configuration is also a model that can be fetched or updated(config.setMyConfig("X").update()).
- for You can share the jar that contains your models with an external product caller or you can restrict the usage of tugmodel only within your product.
- for code interpretation/generation based on metadata. We favor code interpretation over code generation.
- for your own custom metadata. Default implementation is also available for extension.
- for allowing an extensible model where unexpected attributes can be stored by the model and processed by the corresponding tug.

**Demo**
Hello world demo using a User model.
1. Create model. Model classes can also be generated from the provided metadata as long as the metadata follows the general format.
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
    
