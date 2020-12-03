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
			implementation 'com.github.dudziks:mdfirebase:v0.0.5'
	}
  

## 3. Create Model and Entity classes 

	data class MyEntity(var text: String)

	data class MyModel(var name: String)

## 4. Implement intereface *IMapper*

	interface IMapper<From, To> {
		fun map(from: From?): To?
	}

	// From - Entity
	// To - Model


	class MyMapper: FirebaseMapper<MyEntity, MyModel>() {

		override fun map(from: MyEntity?): MyModel? {
			var c: MyModel? = null
			if (from != null) {
				c = MyModel(from.text)
			}
			return c
		}
	}



## 5. implement function *map*

	fun map(from: MyModel?): MyEntity? {
		var c: MyEntity? = null
		if(from!=null){
			c= MyEntity(from.name)
		}
		return c
	}


## 6. Override abstract class *FirebaseDatabaseRepository*
  
	abstract class FirebaseDatabaseRepositor<Entity, Model>(private var mapper: FirebaseMapper<Entity, Model>) {
		/**
		* Implement this function to return root path of your data
		*/
		abstract fun getRootNode(): String

		/**
		* Override this, if you need other root structure than /users/$userId/
		*/
		open fun getRootNodeQuery(fbUser: FirebaseUser): Query =
			FirebaseDatabase.getInstance().reference.child(USERS).child(fbUser.uid).child(getRootNode())

		/**
		* Override this function, if you want to use query on the data.
		*/
		open fun mapDatabaseReference(dbRef: DatabaseReference): Query = dbRef

		...		
	}
	

	class MyRepository(mapper: MyMapper<MyEntity, MyModel>): 
		FirebaseDatabaseRepository<MyEntity, MyModel>(mapper) {

		    /**
			* Implement this function to return root path of your data
			*/
			fun getRootNode(): String { return "myrepoRoot"}

			/**
			* Override this, if you need other root structure than /users/$userId/
			*/
			override fun getRootNodeQuery(fbUser: FirebaseUser): Query =
				FirebaseDatabase.getInstance().reference.child("mygroup").child(fbUser.uid).child(getRootNode())

			/**
			* Override this function, if you want to use query on the data.
			*/
			override fun mapDatabaseReference(dbRef: DatabaseReference): Query {
				return dbRef.orderByChild("creation_date")
			}			
		}

## 7. Implement interface  FirebaseDatabaseRepositoryCallback:FirebaseDatabaseRepositoryCallback<T>


    interface FirebaseDatabaseRepositoryCallback<T> {
        fun onAdded(result: List<T>)
        fun onAdded(result: T)
        fun onError(e: Exception)
        fun onChanged(result: T)
        fun onRemoved(result: T)
        fun onDataChange(result: DataSnapshot)
    }

