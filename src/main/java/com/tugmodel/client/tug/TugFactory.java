/*
 * Copyright (c) 2017- Cristian Donoiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tugmodel.client.tug;

import java.util.List;
import java.util.Map;

import com.tugmodel.client.model.Model;
import com.tugmodel.client.model.config.Config;
import com.tugmodel.client.model.config.tugs.MapperConfig;
import com.tugmodel.client.model.config.tugs.TowConfig;
import com.tugmodel.client.model.config.tugs.TugConfig;
import com.tugmodel.client.model.list.ModelList;
import com.tugmodel.client.model.meta.Meta;
import com.tugmodel.client.model.meta.datatype.DataType;
import com.tugmodel.client.tug.config.IConfigTug;


/**
 * TODO: 1. Consider a variable binding e.g. (modelType=T && modelId=X1, TugA), (modelType=T && modelId=X2, TugB). 2.
 * The tug factory could become also a model. This way obtaining a tug will become itself tugable. static Tug<MyModel> s
 * = TugFactory.s.where("id=factory1").getTug(modelClass).
 * 
 */
@SuppressWarnings("all")
public class Tugs {

    // // Using pair maps allows for lazy initialization of tugs and model classes.
    // private static HashMap<String, String> MODEL_TO_TUG_ID = new HashMap<String, String>();
    // private static HashMap<String, Tug<?>> TUG_ID_TO_TUG_INSTANCE = new HashMap<String, Tug<?>>();
    // static {
    // init(); // Bootstrap.
    // }
    //
    // private static void init() {
    // // Bootstraping starts and ends here. The reading of the default configuration file does not uses Meta or some
    // other tugs.
    // // Only after the bootstraping the Meta and other tugs can be used.
    // ConfigTug tug = new ConfigTug();
    // /////////////////////////////tug.getConfig().setMapper(JacksonMappers.getConfigReaderMapper());
    // // Use this tug for all models until the bindings are initialized. We will only fetch Config model.
    // MODEL_TO_TUG_ID.put(Model.class.getCanonicalName(), "configTug");
    // MODEL_TO_TUG_ID.put(Config.class.getCanonicalName(), "configTug");
    // TUG_ID_TO_TUG_INSTANCE.put("configTug", tug);
//
    //
    // Config config = new Config().fetch();
    // System.out.println(config.toString()); //config.hashCode()
    //
    //
    // // NOTE: Save only config tug since the the config may come from a DB but the config deferal is done in the
    // config file.
    // List<Model> tugs = config.getTugsConfig().getTugs();
    // for (Model tugModel : tugs) {
    // String tugName = tugModel.asString("class");
    // Model meta = config.getMetadataConfig().metadataAsMap().get(tugModel.get("modelId"));
    // if ("Config".equals(tugModel.get("modelId"))) {
    // MODEL_TO_TUG_ID.clear();
    // MODEL_TO_TUG_ID.put(Model.class.getCanonicalName(), tugModel.getId());
    // MODEL_TO_TUG_ID.put(meta.asString("class"), tugModel.getId());
    // TUG_ID_TO_TUG_INSTANCE.clear();
    // try {
    // Tug newConfigTug = (Tug)Class.forName(tugName).newInstance();
    // TugConfig tc = new TugConfig(config.getTugsConfig().mappersAsMap().get(tugModel.get("mapperId")));
    // newConfigTug.setConfig(tc);
    // TUG_ID_TO_TUG_INSTANCE.put(tugModel.getId(), newConfigTug);
    // break;
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw new RuntimeException(e);
    // }
    // }
    // }
    //
    // }
    //
    // public static <M extends Model> Tug<M> get(Class<M> modelClass) {
    //
    // Tug<M> tug = getNearestTug(modelClass);
//
    // return tug;
    // }
    //
    // private static volatile Config CONFIG = null;
    // private static Config parseConfig() {
    // if (CONFIG != null)
    // return CONFIG;
    //
    // CONFIG = new Config().fetch();
    // for (Model tugModel : CONFIG.getTugsConfig().getTugs()) {
    // String tugName = tugModel.asString("class");
    // Model meta = CONFIG.getMetadataConfig().metadataAsMap().get(tugModel.get("modelId"));
    // MODEL_TO_TUG_ID.put(meta.asString("class"), tugModel.getId());
    // // No tug construction is done. Make it lazy.
    // }
    // return CONFIG;
    // }
    //
    // protected static <M extends Model> Tug<M> getNearestTug(Class<M> mClass) {
    // String modelClass = mClass.getCanonicalName();
    // String tugId = MODEL_TO_TUG_ID.get(modelClass);
    // if (tugId != null) {
    // Tug tug = TUG_ID_TO_TUG_INSTANCE.get(tugId);
    // if (tug != null)
    // return tug;
    // }
    //
    // Config config = parseConfig();
    //
    // if (tugId == null) {
    // Class nearestParentClass = Model.class;
    // for (String k : MODEL_TO_TUG_ID.keySet()) {
    // try {
    // Class c = Class.forName(k);
    // if (c.isAssignableFrom(mClass) && nearestParentClass.isAssignableFrom(c)) {
    // nearestParentClass = c;
    // }
    // } catch (ClassNotFoundException e) {
    // throw new RuntimeException(e);
    // }
    // }
    // tugId = MODEL_TO_TUG_ID.get(nearestParentClass.getCanonicalName());
    // // Remember lookup.
    // MODEL_TO_TUG_ID.put(modelClass, tugId);
    // }
//
    // // Before constructing a tug let's check again.
    // Tug tug = TUG_ID_TO_TUG_INSTANCE.get(tugId);
    // if (tug == null) {
    // try {
    // Model tugModel = config.getTugsConfig().tugsAsMap().get(tugId);
    // tug = (Tug)Class.forName(tugModel.asString("class")).newInstance();
    // Model mapper = config.getTugsConfig().mappersAsMap().get(tugModel.asString("mapperId"));
    // TugConfig tc = new TugConfig(tugModel);
    // tug.setConfig(tc);
    // TUG_ID_TO_TUG_INSTANCE.put(tugId, tug);
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // }
    //
    // return tug;
    // }
    // Using pair maps allows for lazy initialization of tugs and model classes.
    // Remember that a tug can serve multiple models.
    // private static HashMap<String, > MODEL_TO_TUG_ID = new HashMap<String, String>();
    // //private static HashMap<String, Tug<?>> TUG_ID_TO_TUG_INSTANCE = new HashMap<String, Tug<?>>();
    // static {
    // // init(); // Bootstrap.
    // }


    private static CrudTug configTug;

    /**
     * Used internally to bootstrap ONLY THE TUG loading configuration by looking in the files:
     * tugmodel-config-defaults.json and tugmodel-config.json(user provided). This is needed since the user may decide
     * to keep the configuration within the database and thus he only needs to provide a tugmodel-config.json that uses
     * a different Tug for the Config. NOTE: If the user keeps the config in the database he will need to also
     * load/store the default config by delegating to the default ConfigTug.
     */
    private static void bootstrapConfigTug() {
        try {
            if (configTug == null) {
                // Not initialised yet. Use a private bootstrap tug that reads the classpath default&custom config.
                Model m = (Model) Class.forName("com.tugmodel.tug.config.BootstrapConfigTug")
                        .getDeclaredMethod("fetch", Model.class).invoke(null, new Model());
                List<Map> tows = m.asList(Config.KEY_TOWS);
                List<Map> tugs = m.asList(Config.KEY_TUGS);
                List<Map> models = m.asList(Config.KEY_MODELS);
                List<Map> mappers = m.asList(Config.KEY_MAPPERS);
                String modelId = null, tugId = null, mapperId = null, tugClass = null;
                Map towConfig = null, tugConfig = null, mapperConfig = null, meta = null;
                
                // Get model id based on model class.
                for (Map model : models) {
                    if (Config.class.getCanonicalName().equals(model.get("class"))) {
                        modelId = (String) model.get("id");
                        meta = model;
                        break;
                    }
                }
                if (modelId == null) {
                    throw new RuntimeException("*** There is no model defined for the model class="
                            + Config.class.getCanonicalName() + ". ***");
                }

                // Get a tow for the model id.
                Map tugConfigInsideTow = null;
                for (Map tow : tows) {
                    // Config model ID is "Config".
                    if ("tm.Config".equals(tow.get(TowConfig.KEY_MODEL_ID))) {
                        towConfig = (Map) tow.get("tug");
                        tugId = (String) towConfig.get("id");
                        break;
                    }
                }
                if (tugId == null) {
                    throw new RuntimeException("*** There is no tow defined for the modelId=" + modelId + ". ***");
                }

                // Get the tug for the tow.
                for (Map tug : tugs) {
                    if (tugId.equals(tug.get("id"))) {
                        tugConfig = tug;
                        tugClass = (String) tug.get("class");
                        mapperId = (String) ((Map) tug.get("mapper")).get("id");
                        break;
                    }
                }
                if (tugId == null) {
                    throw new RuntimeException("*** There is no tow defined for the modelId=" + modelId + ". ***");
                }

                // Get the mapper for the tug.
                for (Map mapper : mappers) {
                    if (mapper.get("id").equals(mapperId)) {
                        mapperConfig = mapper;
                        break;
                    }
                }
                if (mapperConfig == null) {
                    throw new RuntimeException("*** There is no mapper defined for the id =" + mapperId + ". ***");
                }

                // Now merge the mapper.
                mapperConfig.putAll((Map) tugConfig.get("mapper"));
                tugConfig.put("mapper", mapperConfig);
                tugConfig.put("model", meta);

                tugConfig.putAll(towConfig);

                configTug = (CrudTug) Class.forName((String) tugConfig.get("class")).newInstance();
                configTug.getConfig().merge(tugConfig);

                if (configTug == null) {
                    throw new RuntimeException("*** There is no tug defined for the id =" + tugId + ". ***");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can not bootstrap: " + e.getClass() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Use to get the tug manually instead of doing stuff like TowConfig.s which may lead to cycles. Similar with the
     * one above. It also does merging: 1. First the tug.mapper is merged with the corresponding mapper definition. 2.
     * The tug settings inlined inside the tows are merged with a corresponding tug definition.
     */
    private static Tug getForModelViaConfig(String modelId) {
        // Each time I refetch the config. If caching needed do it in the tug since it is business logic!!!.
        Config config = new Config().setId("defaults").fetch();

        String tugId = null, mapperId = null;
        TowConfig towConfig = null;
        TugConfig tugConfig = null; 
        MapperConfig mapperConfig = null;

        Meta model = getConfigTug(config, Meta.class).fetchById(modelId);
        if (model == null) {
            throw new RuntimeException("*** There is no model defined for the model id=" + modelId + ". ***");
        }

        ModelList<TowConfig> tows = getConfigTug(config, TowConfig.class).where("modelId=?", modelId);
        if (tows.isEmpty()) {
            throw new RuntimeException("*** There is no tow defined for the modelId=" + modelId + ". ***");
        }
        towConfig = tows.first();
        tugId = towConfig.getTug().getId();

        tugConfig = getConfigTug(config, TugConfig.class).fetchById(tugId);
        if (tugConfig == null) {
            throw new RuntimeException("*** There is no tug defined for the id =" + tugId + ". ***");
        }
        mapperId = tugConfig.getMapper().getId();
        mapperConfig = getConfigTug(config, MapperConfig.class).fetchById(mapperId);
        if (mapperConfig == null) {
            throw new RuntimeException("*** There is no mapper defined for the id =" + mapperId + ". ***");
        }

        // Now merge 1 in 2 steps (first the mapper then the tug settings from tow).
        MapperConfig mc = mapperConfig.merge(tugConfig.getMapper());
        tugConfig.setMapper(mc);
        tugConfig.merge(towConfig.getTug());
        tugConfig.set("model", model);

        try {
            Tug tug = (Tug) Class.forName(tugConfig.asString("class")).newInstance();
            tug.setConfig(tugConfig);
            return tug;
        } catch (Exception e) {
            throw new RuntimeException("Can not create tug " + tugId + ": " + e.getClass() + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Returns a Tug for the given modelId.
     */
    public static Tug getByModel(String modelId) {
        bootstrapConfigTug();
        if ("tm.Config".equals(modelId)) {
            return configTug;
        }

        Tug tug = getForModelViaConfig(modelId);
        return (CrudTug) tug;
    }

    private static <T extends Model> CrudTug<T> getConfigTug(Config config, Class<T> modelClass) {
        String type = "all";
        // TODO: refactor.
        if (modelClass == Meta.class) {
            type = "models";
        } else if (modelClass == TowConfig.class) {
            type = "tows";
        } else if (modelClass == DataType.class) {
            type = "dataTypes";
        } else if (modelClass == TugConfig.class) {
            type = "tugs";
        } else if (modelClass == MapperConfig.class) {
            type = "mappers";
        }
        // Needs reflection because the ConfigTug is part of the basic set of mappers which is in a separate module.
        IConfigTug<T> tug = (IConfigTug<T>) getByTug("tm.tug.base.configTug");
        tug.workWith(config);
        tug.getConfig().set("type", type);
        return tug;
    }
    /**
     * Returns a Tug for the given modelClass.
     */
    public static Tug getByModel(Class modelClass) {
        bootstrapConfigTug();
        if (modelClass == Config.class) {
            return configTug;
        }

        // Each time I refetch the config. If caching needed do it in the tug since it is business logic!!!.
        Config config = new Config().setId("defaults").fetch();
        CrudTug metaTug = getConfigTug(config, Meta.class);
        ModelList<Meta> models = metaTug.where("class=?", modelClass.getCanonicalName());
        if (models.isEmpty()) {
            throw new RuntimeException(
                    "*** There is no model defined for the model class=" + Config.class.getCanonicalName() + ". ***");
        }

        // Model mm = new Model().set("x", 1);
        Tug tug = getForModelViaConfig(models.first().getId());
        return tug;
    }

    /**
     * Returns CrudTug for the given model class.
     */
    public static <M extends Model> CrudTug<M> getCrud(Class<M> modelClass) {
        return (CrudTug) getByModel(modelClass);
    }

    /**
     * Returns Tug for the given model id.
     */
    public static <M extends Model> CrudTug<M> getCrud(String modelId) {
        return (CrudTug) getByModel(modelId);
    }

    /**
     * @returns a tug for the given tugClass. Think of it as Service(Tug) Factory.
     */
    public static <T extends Tug> T getByTug(Class<T> tugClass) {
        // Each time I refetch the config. If caching needed do it in the tug since it is business logic!!!.
        Config config = new Config().setId("defaults").fetch();

        CrudTug tug = getConfigTug(config, TugConfig.class);
        ModelList<TugConfig> tcList = tug.where("class=?", tugClass.getCanonicalName());
        if (tcList.isEmpty()) {
            throw new RuntimeException(
                    "*** There is no tug defined for the tug class=" + tugClass.getCanonicalName() + ". ***");
        }
        return (T) getByTug(tcList.first().getId());
    }

    public static Tug getByTug(String tugId) {
        // Each time I refetch the config. If caching needed do it in the tug since it is business logic!!!.
        Config config = new Config().setId("defaults").fetch();

        String mapperId = null, tugClassName = null;
        TugConfig tugConfig = null;
        MapperConfig mapperConfig = null;

        // Get the tug for the tow.
        for (TugConfig tug : config.getTugs()) {
            if (tug.getId().equals(tugId)) {
                tugConfig = tug;
                tugClassName = tug.asString("class");
                mapperId = tug.getMapper().getId();
                break;
            }
        }
        if (tugConfig == null) {
            throw new RuntimeException("*** There is no tug defined for the tug class =" + tugClassName + ". ***");
        }

        // Get the mapper for the tug.
        for (MapperConfig mapper : config.getMappers()) {
            if (mapperId.equals(mapper.getId())) {
                mapperConfig = mapper;
                break;
            }
        }
        if (mapperConfig == null) {
            throw new RuntimeException("*** There is no mapper defined for the id =" + mapperId + ". ***");
        }

        // Now merge the mapper.
        MapperConfig mc = mapperConfig.merge(tugConfig.getMapper());
        tugConfig.setMapper(mc);
        try {
            Tug tug = (Tug) Class.forName(tugConfig.asString("class")).newInstance();
            tug.setConfig(tugConfig);
            return tug;
        } catch (Exception e) {
            throw new RuntimeException("Can not create tug " + tugId + ": " + e.getClass() + ": " + e.getMessage(), e);
        }
    }


}
