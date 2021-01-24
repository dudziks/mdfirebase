# mdfirebase
Tool for accessing firebase realtime database


# How to use it

## 1. Add the JitPack repository to your build file

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
  
##  2. Add the dependency:
  
	dependencies {
			implementation 'com.github.dudziks:mdfirebase:v0.0.9'
	}
  

## 3. Create Model and Entity classes 

	data class MyDTO(var text: String)

	data class MyModel(var name: String)

## 4. Implement intereface *IMapper*

	interface IMapper<DTO, Model>  {
	    fun mapFromDTO(from: DTO?): Model?
	    fun mapToDTO(from: Model?): DTO?
	}

	// MyDTO - Entity
	// MyModel - Model

	class MyMapper:  FirebaseMapper<MyDTO, MyModel>() {
   		override fun mapFromDTO(from: MyDTO?): MyModel? =
        		from?.run { MyModel(contents = text, theKey = key) }

	   	override fun mapToDTO(from: MyModel?): MyDTO? =
			from?.run { MyDTO(text = contents, key = theKey) }
	}


## 6 Use instance of interface IFirebaseDAO<MyDTO, MyModel> for accessing the data.

Example of class using instance of IFirebaseDAO<MyDTO, MyModel>

class MyRepo {

	private val dao: IFirebaseDAO<MyDTO, MyModel> =  FirebaseDAO.Builder<MyDTO, MyModel>()
                .fbUser(Firebase.auth.currentUser)
                .rootNodePath { "path/to/my/data" }
                .mapper(MyMapper())
                .build()


	@ExperimentalCoroutinesApi
	fun getData(): Flow<MmyModel?> =callbackFlow {
		dao.addListener(DefaultFirebaseDBCallback(
		    onAddedCallback = { offer(it) }
		    //onChangedCallback = { /* manage changed data */}
		    //onAddedListCallback = { /* manage changed data */}
		    //onRemovedCallback = { /* manage removed data */}
		    //onDataChangedCallback = { /* manage changed data */}
		    //onErrorCallback = { /* manage error */}		  
		    
		))
		awaitClose { dao.removeListener()
	}	
}
   
You can override all the callbacks commented out. There are empty default implementation of each callback, so if you wont implement it, it will do nothing.
   
		 

## 7. Use the class MyReo in your Viewmodel

class MyViewModel: ViewModel() {
	val myRepo = MyRepo()

	val liveData = liveData(Dispatchers.IO) {
		myRepo.getData().collect { value -> emit(value) }
	    }
}

