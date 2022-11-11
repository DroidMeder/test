package kg.o.internlabs.omarket.data.local.prefs

import kg.o.internlabs.core.data.local.prefs.StoragePreferences
import kg.o.internlabs.omarket.domain.repository.PrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrefsRepositoryImpl @Inject constructor(private val context: StoragePreferences) : PrefsRepository {

    override fun checkPhoneNumberFromPrefs(number: String): Flow<Boolean> {
        val phoneNumber = context.userPhoneNumber
        //return (phoneNumber?.isNotEmpty() ?: false)
        if (phoneNumber != null) {
            if (phoneNumber.isNotEmpty()){
                return true.asFlow()
            }


        }
    }

}