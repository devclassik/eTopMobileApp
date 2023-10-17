package com.gbikna.sample.etop.kimono;


class Dukpt {
    companion object {
        @JvmStatic
        val ipek = "3F2216D8297BCE9C"
        val ipekTest = "9F8011E7E71E483B"

        @JvmStatic
        fun getSessionKey(ipek: String, iksn: String) : String {
            val data = ISOUtil.hexXor(ipek, iksn)
            val desResult = CryptoHelper().desEncrypt(ipek, data)

            return ISOUtil.hexXor(desResult, ipek)
        }

        @JvmStatic
        fun getPinBlock(workingKey: String, pin: String, pan: String) : String {
            val clearPinBlock = PinblockTool.format0Encode(pin, pan)
            var pinBlock = ISOUtil.hexXor(workingKey, clearPinBlock)
            pinBlock = CryptoHelper().desEncrypt(workingKey, pinBlock)
            return ISOUtil.hexXor(workingKey, pinBlock);
        }

        @JvmStatic
        fun getPinBlock(workingKey: String, clearPinBlock: String) : String {
            var pinBlock = ISOUtil.hexXor(workingKey, clearPinBlock)
            pinBlock = CryptoHelper().desEncrypt(workingKey, pinBlock)

            return ISOUtil.hexXor(workingKey, pinBlock)
        }

        @JvmStatic
        fun blackBoxLogic(ipek: String, ksn: String): String {
            if (ipek.length < 32) {
                return getSessionKey(ipek, ksn)
            }

            val current_sk = ipek
            val ksn_mod = ksn

            val leftIpek = ISOUtil.hexAnd(current_sk, "FFFFFFFFFFFFFFFF0000000000000000").substring(0, 16)
            val rightIpek = ISOUtil.hexAnd(current_sk, "0000000000000000FFFFFFFFFFFFFFFF").substring(16)

            val message = ISOUtil.hexXor(rightIpek, ksn_mod)
            val desresult = CryptoHelper().desEncrypt(leftIpek, message)

            val rightSessionKey = ISOUtil.hexXor(desresult, rightIpek)
            val resultCurrent_sk = ISOUtil.hexXor(current_sk, "C0C0C0C000000000C0C0C0C000000000")

            val leftIpek2 = ISOUtil.hexAnd(resultCurrent_sk, "FFFFFFFFFFFFFFFF0000000000000000").substring(0, 16)
            val rightIpek2 = ISOUtil.hexAnd(resultCurrent_sk, "0000000000000000FFFFFFFFFFFFFFFF").substring(16)
            val message2 = ISOUtil.hexXor(rightIpek2, ksn_mod)
            val desresult2 = CryptoHelper().desEncrypt(leftIpek2, message2)

            val leftSessionKey = ISOUtil.hexXor(desresult2, rightIpek2)

            return leftSessionKey + rightSessionKey
        }

        @JvmStatic
        fun getWorkingKey(IPEK: String, KSN: String): String {
            var initialIPEK = IPEK
            val ksn = KSN.padStart(20, '0')
            var sessionkey = ""

            //Get ksn with a zero counter by ANDing it with FFFFFFFFFFFFFFE00000
            val newKSN = ISOUtil.hexAnd(ksn, "0000FFFFFFFFFFE00000")
            val counterKSN = ksn.substring(ksn.length - 5).padStart(16, '0')

            //get the number of binary associated with the counterKSN number
            var newKSNtoleft16 = newKSN.substring(newKSN.length - 16)
            val counterKSNbin = counterKSN.toUInt().toString(2)

            var binarycount = counterKSNbin
            for (i in counterKSNbin.indices) {
                val len: Int = binarycount.length

                var result = ""
                if (binarycount.substring(0, 1) == "1") {
                    result = "1".padEnd(len, '0')
                    binarycount = binarycount.substring(1)
                }
                else {
                    binarycount = binarycount.substring(1)
                    continue
                }

                val counterKSN2 = result.toUInt(2).toString(16).padStart(16, '0')
                val newKSN2 = ISOUtil.hexOr(newKSNtoleft16, counterKSN2)

                //Call the blackbox from here
                sessionkey = blackBoxLogic(initialIPEK, newKSN2)
                newKSNtoleft16 = newKSN2
                initialIPEK = sessionkey
            }

            return ISOUtil.hexXor(sessionkey, "00000000000000FF00000000000000FF").substring(0, 16)
        }

        @JvmStatic
        fun getWorkingKey(KSN: String): String {
            return getWorkingKey(ipek, KSN)
        }
    }
}