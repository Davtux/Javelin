package fr.unilim.talos;

import gov.nasa.jpf.jdart.Symbolic;
import javacard.framework.APDUException;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;
import javacard.framework.Util;

/**
 * Application Protocol Data Unit (APDU) is
 * the communication format between the card and the off-card applications.
 * The format of the APDU is defined in ISO specification 7816-4.<p>
 *
 * This class only supports messages which conform to the structure of
 * command and response defined in ISO 7816-4. The behavior of messages which
 * use proprietary structure of messages ( e.g with header CLA byte in range D0-FE ) is
 * undefined. Additionally, this class does not support extended length fields.<p>
 *
 * APDU objects are owned by the JCRE. The APDU class maintains a byte array buffer which is
 * used to transfer incoming APDU header and data bytes as well as outgoing data. The buffer
 * length must be at least 37 bytes.<p>
 *
 * The applet receives an APDU instance to process from
 * the JCRE in the <code>Applet.process(APDU)</code> method, and
 * the first five bytes [ CLA, INS, P1, P2, P3 ] are available
 * in the APDU buffer.<p>
 *
 * The APDU class API is designed to be transport protocol independent.
 * In other words, applets can use the same APDU methods regardless of whether
 * the underlying protocol in use is T=0 or T=1 (as defined in ISO 7816-3).<p>
 * Depending on the size of the incoming APDU data,
 * it may not fit inside the buffer and
 * may need to be read in portions by the applet. Depending on the size of the
 * outgoing response APDU data, it may not fit inside the buffer and may
 * need to be written in portions by the applet. The APDU class has methods
 * to facilitate this.<p>
 *
 * For sending large byte arrays as response data,
 * the APDU class provides a special method <code>sendBytesLong()</code> which
 * manages the APDU buffer.<p>
 *
 * <pre>
 * // The purpose of this example is to show most of the methods
 * // in use and not to depict any particular APDU processing
 *
 *public void process(APDU apdu){
 *  // ...
 *  byte[] buffer = apdu.getBuffer();
 *  byte cla = buffer[ISO7816.OFFSET_CLA];
 *  byte ins = buffer[ISO7816.OFFSET_INS];
 *  ...
 *  // assume this command has incoming data
 *  // Lc tells us the incoming apdu command length
 *  short bytesLeft = (short) (buffer[ISO7816.OFFSET_LC] & 0x00FF);
 *  if (bytesLeft < ...) ISOException.throwIt( ISO7816.SW_WRONG_LENGTH );
 *
 *  short readCount = apdu.setIncomingAndReceive();
 *
 *  //
 *  //...
 *  //
 *  // Note that for a short response as in the case illustrated here
 *  // the three APDU method calls shown : setOutgoing(),setOutgoingLength() & sendBytes()
 *  // could be replaced by one APDU method call : setOutgoingAndSend().
 *
 *  // construct the reply APDU
 *  short le = apdu.setOutgoing();
 *  if (le < 2) ISOException.throwIt( ISO7816.SW_WRONG_LENGTH );
 *  apdu.setOutgoingLength( (short)3 );
 *
 *  // build response data in apdu.buffer[ 0.. outCount-1 ];
 *  buffer[0] = (byte)1; buffer[1] = (byte)2; buffer[3] = (byte)3;
 *  apdu.sendBytes ( (short)0 , (short)3 );
 *  // return good complete status 90 00
 *  }
 * </pre>
 *
 */
public final class APDU
{
    /** ISO 7816 transmission protocol type T=0 */
    public static final byte PROTOCOL_T0 = 0;
    /** ISO 7816 transmission protocol type T=1 */
    public static final byte PROTOCOL_T1 = 1;

//    private static final byte LEN_BAP = 54;
    private static final short BUFFERSIZE = 6; //Marouan: original was : 263

    /** The APDU buffer */
    @Symbolic("true")
    private byte[] buffer;
   
    private static APDU theAPDU;
    private static short lc = (short)0;
    private static short le = (short)0;
    private static short outLength = 0;


    byte state;
    public static final byte STATE_INIT = 0;
   

    public APDU() {
        state = 0;
        buffer = new byte[BUFFERSIZE];
    }

    /**
     * Returns the APDU buffer byte array.
     * @return byte array containing the APDU buffer
     */
    public byte[] getBuffer() {
      state = 0;
      outLength = 0;
      return buffer;
    }

   
    public void setBuffer(byte[] in) {
            Util.arrayCopy(in, (short)0, this.buffer, (short)0, (short)in.length);
        state = 0;
        outLength = 0;
      }
   
    /**
     * Returns the configured incoming block size.
     * In T=1 protocol, this corresponds to the maximum size of incoming data
     * blocks from the CAD, IFSC (information field size for ICC).
     * In T=0 protocol, this method returns 1.
     * IFSC is defined in ISO 7816-3.
     * This information may be used to ensure that there is enough space
     * remaining in the APDU buffer when <code>receiveBytes()</code>
     * is invoked.
     *
     * <p>Notes:<ul>
     *      <li><em> On </em><code>receiveBytes()</code> the
     *              <code>bOff</code> param should account for this
     *              potential blocksize.</em>
     * </ul>
     *
     * @return incoming block size setting.
     *
     * @see #receiveBytes
     */
    public static short getInBlockSize() {
            return (short)1;
    }

    /**
     * Returns the configured outgoing block size.
     */
    public static short getOutBlockSize() {
            return (short)258;
    }

    /**
     * Returns the ISO 7816 transport protocol type in progress.
     *
     * @return the protocol type in progress. One of
     * PROTOCOL_T0, PROTOCOL_T1 listed above.
     */
    public static byte getProtocol() {
        return APDU.PROTOCOL_T0 ;
    }

    /**
     * In T=1 protocol, this method returns the Node Address byte, NAD.
     * In T=0 protocol, this method returns 0.
     * This may be used as additional information to maintain multiple contexts.
     *
     * @return NAD transport byte as defined in ISO 7816-3.
     */
    public byte getNAD() {
             return (byte)0;
    }


    /**
     * This method is used to set the data transfer direction to
     * outbound and to obtain the expected length of response (Le).
     *
     * <p>Notes: <ul>
     *      <li><em> Any remaining incoming data will be discarded</em>
     *      <li><em> T=0 (Case 4) protocol, this method will return 256</em>
     * </ul>
     *
     * @return Le, the expected length of response.
     *
     * @exception APDUException with the following reason codes:<ul>
     *      <li><code>APDUException.ILLEGAL_USE</code> if this method or
     *          <code>setOutgoingNoChaining</code> already invoked.
     *
     *      <li><code>APDUException.IO_ERROR</code> on I/O error.
     * </ul>
     */
    public short setOutgoing() throws APDUException {
            Util.arrayFillNonAtomic(buffer, (short)0, (short)buffer.length, (byte)0);
        switch( state )
        {
            case STATE_INIT: state = 2; break;
            case 1: state = 4; break;

            default: APDUException.throwIt( APDUException.ILLEGAL_USE );
        }
        return le;
    }

    /**
     * This method is used to set the data transfer direction to outbound
     * without using BLOCK CHAINING and to obtain the expectend length of
     * response (Le). This method should be used inplace of the
     * <code>setOutgoing()</code> method by applets which need to be
     * compatible with legacy CAD/terminals which do not support ISO 7816-3/4
     * defined block chaining. See <em>JCRE Specification</em> for details.
     *
     * <P>Notes: <ul>
     *      <li><em> Any remaining incoming data will be discarded.</em>
     *      <li><em> In T=0 (Case 4) protocol, this method will return 256.</em>
     *      <li><em> When this method is used, the <code>waitExtension</code>
     *               method cannot be used.</em>
     *      <li><em> In T=1 protocol, retransmission on error may be
     *               restricted.</em>
     *      <li><em> In T=0 protocol, the outbound transfer must be performed
     *               without response status chaining.</em>
     *      <li><em> In T=1 protocol, the outbound transfer must not set the More(M)
     *               Bit in the PCB of the 1 block (see ISO 7816-3).</em>
     * </ul>
     *
     * @return Le, the expected length of response data.
     *
     * @exception APDUException with the following reason codes:<ul>
     *
     *      <li><code>APDUException.ILLEGAL_USE</code> if this method or
     *          <code>setOutgoing</code> already invoked.
     *
     *      <li><code>APDUException.IO_ERROR</code> on I/O error.
     * </ul>
     *
     */
    public short setOutgoingNoChaining() throws APDUException {
        APDUException.throwIt( APDUException.IO_ERROR );
        return 0;
    }

    /**
     * Sets the expected length of response data. Default is 0.
     *
     * <p>Note:<ul>
     *      <li><em> In T=0 (Case 2 & 4) protocol, the length is used
     *               by the JCRE to prompt the CAD for GET RESPONSE
     *               commands.</em>
     * </ul>
     *
     * @param len the length of response data.
     *
     * @exception APDUException with the following reason codes:<ul>
     *      <li><code>APDUException.ILLEGAL_USE</code> if
     *          <code>setOutgoing()</code> not called or this method
     *          already invoked.
     *
     *      <li><code>APDUException.BAD_LENGTH</code> if <code>len</code>
     *          is greater than 256.
     *
     *      <li><code>APDUException.IO_ERROR</code> on I/O error.
     * </ul>
     */
    public void setOutgoingLength(short len) throws APDUException {
        switch( state )
        {
            case 2:
            case 4: state = 3; break;

            default: APDUException.throwIt( APDUException.ILLEGAL_USE );
        }
        if( len > 256 )
            APDUException.throwIt( APDUException.BAD_LENGTH );
        outLength = len;
    }

    /**
     * Gets as many data bytes as will safely fit (without APDU buffer overflow)
     * at the specified offset <code>bOff</code>. Gets all the remaining bytes
     * if it fits.
     *
     * <p>Notes:<ul>
     *      <li><em> The space in the buffer must allow for incoming block
     *               size</em>
     *
     *      <li><em> In T=1 protocol, if all the remaining bytes do not fit in
     *               the buffer, this method return less bytes than the maximum
     *               incoming block size (IFSC).</em>
     *
     *      <li><em> In T=0 protocol, if all the remaining bytes do not fit in
     *               the buffer, this method may return less than a full buffer
     *               of bytes to optimize and reduce protocol overhead.</em>
     * </ul>
     *
     * @param bOff the offset into APDU buffer.
     *
     * @return number of bytes read. 0 if no bytes are available.
     *
     * @exception APDUException with the following reason codes:<ul>
     *      <li><code>APDUException.ILLEGAL_USE</code> if
     *          <code>setIncomingAndReceive()</code> not called.
     *
     *      <li><code>APDUException.BUFFER_BOUNDS</code> if not enough
     *          buffer space for incoming block size.
     *
     *      <li><code>APDUException.IO_ERROR</code> on I/O error.
     * </ul>
     *
     * @see #getInBlockSize
     */
    public short receiveBytes(short bOff) throws APDUException {
        short nbBytes;

        if( state != 1 )
            APDUException.throwIt(APDUException.ILLEGAL_USE);

        if( (getInBlockSize() + bOff) > buffer.length )
            APDUException.throwIt(APDUException.BUFFER_BOUNDS);

        nbBytes = receiveBytesS(buffer,bOff,(short)(buffer.length-bOff));
        return nbBytes;
    }

   
    public static short receiveBytesS(byte[] buffer, short offset, short length) {
        short sLength = (short)Math.min((int)length, (int)lc);
        if((sLength != 0) && (lc != (short)0)) {
    //        Util.arrayCopyNonAtomic(bCmdBuffer, (short)5, buffer, (short)offset, (short)(sLength));
            lc -= sLength;
        }
        return sLength;
    }
    /**
     * This is the primary receive method. Calling this method indicates that
     * this APDU has incoming data. This method gets as many bytes as will
     * safely fit without buffer overflow in the APDU buffer following the header.
     * Gets all the incoming bytes if it fits.
     *
     * <p>Notes:<ul>
     *      <li><em> In T=0 ( Case 3&4 ) protocol the P3 param is assumed
     *               to be Lc.</em>
     *
     *      <li><em> Data is read into the buffer at offset 5.</em>
     *
     *      <li><em> In T=1, if all the incoming bytes do not fit in the
     *               buffer, this method may return less bytes than the maximum
     *               incoming block size (IFSC).</em>
     *
     *      <li><em> In T=0 protocol, if all the incoming bytes do not fit in
     *               the buffer, this method may return less than a full buffer
     *               of bytes to optimize and reduce protocol overhead.</em>
     *
     *      <li><em> This method sets the transfer direction to be inbound and
     *               calls </em><code>receiveBytes(5)</code><em>.</em>
     *
     *      <li><em> This method may only be called once in a</em>
     *               <code>Applet.process()</code><em> method.</em>
     * </ul>
     *
     * @return number of bytes read. Returns 0 if no bytes available.
     *
     * @exception APDUException with the following reason codes:<ul>
     *
     *      <li><code>APDUException.ILLEGAL_USE</code> if
     *          <code>setIncomingAndReceive()</code> already invoked or if
     *          <code>setOutgoing()</code> or <code>setOutgoingNoChaining()</code>
     *          previously invoked.
     *
     *      <li><code>APDUException.IO_ERROR</code> on I/O error.
     * </ul>
     */
    public short setIncomingAndReceive() throws APDUException {

        if (state != STATE_INIT)
            APDUException.throwIt(APDUException.ILLEGAL_USE);
        state = 1;
        return receiveBytes((short) 5);
    }

    /**
     * Sends <code>len</code> more bytes from APDU buffer at specified
     * offset <code>bOff</code>.
     *
     * <P>If the last of the response is being sent by the invokation
     * of this method, the APDU buffer must not be altered. If the data
     * is altered, incorrect output may be sent to the CAD.
     * This allows the implementation to reduce protocol overhead
     * by transmitting the last part of the response alongwith the
     * status bytes.
     *
     * <P>Notes:<ul>
     *      <li><em> If </em><code>setOutgoingNoChaining()</code><em> was invoked,
     *               output block chaining must not be used.</em>
     *      <li><em> In T=0 protocol, if </em><code>setOutgoingNoChaining()</code>
     *               <em>was invoked, Le bytes must be transmitted before
     *               response status is returned.</em>
     *      <li><em> In T=0 protocol, if this method throws an </em>
     *               <code>APDUException</code><em> with </em>
     *               <code>NO_T0_GETRESPONSE</code><em> reason code, the JCRE
     *               will restart APDU command processing using the
     *               newly received command. No more output data can
     *               be transmitted. No error status response can be
     *               returned.</em>
     * </ul>
     *
     * @param bOff the offset into APDU buffer.
     * @param len the length of the data in bytes to send.
     *
     * @exception APDUException with the following reason codes:<ul>
     *
     *      <li> <code>APDUException.ILLEGAL_USE</code> if
     *           <code>setOutgoingLen()</code> not called or
     *           <code>setOutgoingAndSend()</code> previously
     *           invoked or response byte count exeeded or if
     *           <code>APDUException.NO_T0_GETRESPONSE</code> previously
     *           thrown.
     *
     *      <li> APDUException.BUFFER_BOUNDS if the sum of <code>bOff</code>
     *           and <code>len</code> exceeds the buffer size.
     *
     *      <li> APDUException.IO_ERROR on I/O error.
     *
     *      <li> APDUException.NO_T0_GETRESPONSE if T=0 protocol is in use and
     *           the CAD does not respond to ISO7816.SW_BYTES_REMAINING_00+
     *           <count> response status with GET RESPONSE command.
     *</ul>
     *
     * @see #setOutgoing
     * @see #setOutgoingNoChaining
     */
    public void sendBytes(short bOff, short len) throws APDUException {
        sendBytesLong(buffer, bOff, len);
    }


    /**
     * Sends <code>len</code> more bytes from APDU buffer at specified
     * offset <code>bOff</code>.
     *
     * <P>If the last of the response is being sent by the invokation
     * of this method, the APDU buffer must not be altered. If the data
     * is altered, incorrect output may be sent to the CAD.
     * This allows the implementation to reduce protocol overhead
     * by transmitting the last part of the response alongwith the
     * status bytes.
     *
     * <P>The JCRE may use the APDU buffer to send data to the CAD.
     *
     * <P>Notes:<ul>
     *      <li><em> If </em><code>setOutgoingNoChaining()</code><em> was invoked,
     *               output block chaining must not be used.</em>
     *      <li><em> In T=0 protocol, if </em><code>setOutgoingNoChaining()</code>
     *               <em>was invoked, Le bytes must be transmitted before
     *               response status is returned.</em>
     *      <li><em> In T=0 protocol, if this method throws an
     *               </em><code>APDUException</code><em> with
     *               </em><code>NO_T0_GETRESPONSE</code><em> reason code,
     *               the JCRE will restart APDU command processing using the
     *               newly received command. No more output data can
     *               be transmitted. No error status response can be
     *               returned.</em>
     * </ul>
     *
     * @param outData the source data byte array.
     * @param bOff the offset into outData array.
     * @param len the byte length of the data to send.
     *
     * @exception APDUException with the following reason codes:<ul>
     *
     *      <li> <code>APDUException.ILLEGAL_USE</code> if
     *           <code>setOutgoingLen()</code> not called or
     *           <code>setOutgoingAndSend()</code> previously
     *           invoked or response byte count exeeded or if
     *           <code>APDUException.NO_T0_GETRESPONSE</code> previously
     *           thrown.
     *
     *      <li> APDUException.BUFFER_BOUNDS if the sum of <code>bOff</code>
     *           and <code>len</code> exceeds the buffer size.
     *
     *      <li> APDUException.IO_ERROR on I/O error.
     *
     *      <li> APDUException.NO_T0_GETRESPONSE if T=0 protocol is in use and
     *           the CAD does not respond to ISO7816.SW_BYTES_REMAINING_00+
     *           <count> response status with GET RESPONSE command.
     *</ul>
     *
     * @see #setOutgoing
     * @see #setOutgoingNoChaining
     */
    public void sendBytesLong(byte[] outData, short bOff, short len) throws APDUException {
        if ( state != 3 )
            APDUException.throwIt(APDUException.ILLEGAL_USE);

        if ( bOff+len > outData.length )
            APDUException.throwIt(APDUException.BUFFER_BOUNDS);
    }

  /**
   * This is the "convenience" send method. It provides for the most efficient
   * way to send a short response which fits in the buffer and needs the least
   * protocol overhead.
   * This method is a combination of <code>setOutgoing(), setOutgoingLength(
   * len )</code> followed by <code>sendBytes( bOff, len )</code>. In
   * addition, once this method is invoked, <code>sendBytes</code> and
   * <code>sendBytesLong</code> methods cannot be invoked and the APDU
   * buffer must not be altered.<p>
   *
   * Sends <code>len</code> byte response from the APDU buffer at
   * starting specified offset <code>bOff</code>.
   *
   * <p>Notes:<ul>
   *    <li><em>No other APDU send methods can be invoked.</em>
   *    <li><em>The APDU buffer must not be altered. If the data is altered,
   *            incorrect output may be sent to the CAD.</em>
   *    <li><em>The actual data transmission may only take place on return
   *            from <code>Applet.prodess()</code></em>
   *    </ul>
   *
   * @param bOff the offset into APDU buffer.
   * @param len the bytelength of the data to send.
   *
   * @exception APDUException with the following reason codes:<ul>
   *
   *    <li><code>APDUException.ILLEGAL_USE</code> if <code>setOutgoing()</code>
   *        or <code>setOutgoingAndSend()</code> previously invoked
   *        or response byte count exeeded.
   *
   *    <li><code>APDUException.IO_ERROR</code> on I/O error.</ul>
   *
   */
    public void setOutgoingAndSend( short bOff, short len) throws APDUException, ISOException {
        setOutgoing();
        setOutgoingLength(len);
        sendBytes( bOff, len );
    }


  /**
   * Requests additional processsing time from CAD. The implementation should
   * ensure that this method needs to be invoked only under unusual conditions
   * requiring excessive processing times.
   *
   * <p>Notes:<ul>
   *    <li><em>In T=0 protocol, a NULL procedure byte is sent to reset the
   *            work waiting time (see ISO 7816-3).</em>
   *
   *    <li><em>In T=1 protocol, the implementation needs to request the same
   *            T=0 protocol work waiting time quantum by sending a T=1
   *            protocol request for wait time extension(see ISO 7816-3).</em>
   *
   *    <li><em>If the implementation uses an automatic timer mechanism
   *            instead, this method may do nothing.</em>
   * </ul>
   *
   * @exception APDUException with the following reason codes:<ul>
   *
   *    <li><code>APDUException.ILLEGAL_USE</code> if
   *        <code>setOutgoingNoChaining()</code> previously invoked.
   *
   *    <li><code>APDUException.IO_ERROR</code> on I/O error.</ul>
   *
   */
    public static void waitExtension() {

    }
    /**
     * Finish by the JCRE the sending of the APDU, it inserts the
     * status word except if an exception is thrown
     * @return
     */
    public void JCRE_return (){ 
            this.buffer[outLength]= (byte) 0x90;
            this.buffer[outLength+1] = 0;
            outLength=0;
    }
    /**
     * While catching the exception thrown by the process method
     * call the getreason() on the object and call this method
     * @param sw
     */
    public void JCRE_return_Exception (short sw){ 
            this.buffer[0]= (byte) (sw >> 8);
            this.buffer[1] = (byte) (sw & (short)0xFF);
            outLength=0;
    }
}
