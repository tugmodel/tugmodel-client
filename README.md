# tugmodel-client

# Manifesto:

This project wants to be a framework designed to make developers life easier. The following ideas will be implemented:
  1. [Direct operations](#direct-operations) ([active record pattern](https://en.wikipedia.org/wiki/Active_record_pattern)).
  2. Simple [workflow or tug](#simple-tug-workflow) for decoupling.
  3. [No more SQL seeding](#no-more-sql-seeding-scripts). Use JSON for seeds.
  
The implementation language will be Java. No external library dependencies will be needed except Jackson JSON.
Similar patterns in: Ruby ActiveModel, JavaLite and Backbone.js.        

#Direct operations
 ```java
 //MyControlService.save(myObj);
 myObj.save();
 ```
Instead of doing `MyControlService.save(myObj)` you will do **`myObj.save()`**.
The benefit is that it allows an abstraction of the operation and transport(e.g. the destination could be a local/remote service or a rest/non rest provider depending on the configured proxy). This is inspired by Martin Fowler's [active record pattern](https://en.wikipedia.org/wiki/Active_record_pattern) and backbonejs.

#Simple tug workflow
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
  * **GUI web app** for `tug` (simple workflow) configuration and stats reports.  
  * [JSON-API](http://jsonapi.org/) proxy implementation.
  * Integrate a [free](https://github.com/Activiti) or develop a complex workflow [BPMN](https://en.wikipedia.org/wiki/Business_Process_Model_and_Notation) as alternative to `tugmodel` simple workflow. 
  * Workflow change detection.  
  * Tug task coordinator needs capability querying(`isAbleFor(feature/task)`) so that it does not execute tasks that it can not coordinate.
  * Scriptable installation [CLI](https://en.wikipedia.org/wiki/Command-line_interface) called "tug" callable from product installler (for JSON seeds), updates or new packages. Follows Martin Fowler's [microservices](https://martinfowler.com/articles/microservices.html) deployment model.
  * Rest level 2,3 proxy.

# License #
`tugmodel` is licensed under the [Apache License 2.0] (https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)). Click [link] (https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)) for more details.
    
    
