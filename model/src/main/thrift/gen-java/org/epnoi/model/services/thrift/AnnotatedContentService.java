/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.epnoi.model.services.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotatedContentService {

  public interface Iface {

    public AnnotatedDocument getAnnotatedContent(String uri, String type) throws org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void getAnnotatedContent(String uri, String type, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

    public AnnotatedDocument getAnnotatedContent(String uri, String type) throws org.apache.thrift.TException
    {
      send_getAnnotatedContent(uri, type);
      return recv_getAnnotatedContent();
    }

    public void send_getAnnotatedContent(String uri, String type) throws org.apache.thrift.TException
    {
      getAnnotatedContent_args args = new getAnnotatedContent_args();
      args.setUri(uri);
      args.setType(type);
      sendBase("getAnnotatedContent", args);
    }

    public AnnotatedDocument recv_getAnnotatedContent() throws org.apache.thrift.TException
    {
      getAnnotatedContent_result result = new getAnnotatedContent_result();
      receiveBase(result, "getAnnotatedContent");
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getAnnotatedContent failed: unknown result");
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void getAnnotatedContent(String uri, String type, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
      checkReady();
      getAnnotatedContent_call method_call = new getAnnotatedContent_call(uri, type, resultHandler, this, ___protocolFactory, ___transport);
      this.___currentMethod = method_call;
      ___manager.call(method_call);
    }

    public static class getAnnotatedContent_call extends org.apache.thrift.async.TAsyncMethodCall {
      private String uri;
      private String type;
      public getAnnotatedContent_call(String uri, String type, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.uri = uri;
        this.type = type;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getAnnotatedContent", org.apache.thrift.protocol.TMessageType.CALL, 0));
        getAnnotatedContent_args args = new getAnnotatedContent_args();
        args.setUri(uri);
        args.setType(type);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public AnnotatedDocument getResult() throws org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        return (new Client(prot)).recv_getAnnotatedContent();
      }
    }

  }

  public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
    }

    protected Processor(I iface, Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends Iface> Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> getProcessMap(Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      processMap.put("getAnnotatedContent", new getAnnotatedContent());
      return processMap;
    }

    public static class getAnnotatedContent<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getAnnotatedContent_args> {
      public getAnnotatedContent() {
        super("getAnnotatedContent");
      }

      public getAnnotatedContent_args getEmptyArgsInstance() {
        return new getAnnotatedContent_args();
      }

      protected boolean isOneway() {
        return false;
      }

      public getAnnotatedContent_result getResult(I iface, getAnnotatedContent_args args) throws org.apache.thrift.TException {
        getAnnotatedContent_result result = new getAnnotatedContent_result();
        result.success = iface.getAnnotatedContent(args.uri, args.type);
        return result;
      }
    }

  }

  public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class.getName());
    public AsyncProcessor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
    }

    protected AsyncProcessor(I iface, Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends AsyncIface> Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> getProcessMap(Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      processMap.put("getAnnotatedContent", new getAnnotatedContent());
      return processMap;
    }

    public static class getAnnotatedContent<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, getAnnotatedContent_args, AnnotatedDocument> {
      public getAnnotatedContent() {
        super("getAnnotatedContent");
      }

      public getAnnotatedContent_args getEmptyArgsInstance() {
        return new getAnnotatedContent_args();
      }

      public AsyncMethodCallback<AnnotatedDocument> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
        final org.apache.thrift.AsyncProcessFunction fcall = this;
        return new AsyncMethodCallback<AnnotatedDocument>() { 
          public void onComplete(AnnotatedDocument o) {
            getAnnotatedContent_result result = new getAnnotatedContent_result();
            result.success = o;
            try {
              fcall.sendResponse(fb,result, org.apache.thrift.protocol.TMessageType.REPLY,seqid);
              return;
            } catch (Exception e) {
              LOGGER.error("Exception writing to internal frame buffer", e);
            }
            fb.close();
          }
          public void onError(Exception e) {
            byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
            org.apache.thrift.TBase msg;
            getAnnotatedContent_result result = new getAnnotatedContent_result();
            {
              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
              msg = (org.apache.thrift.TBase)new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
            }
            try {
              fcall.sendResponse(fb,msg,msgType,seqid);
              return;
            } catch (Exception ex) {
              LOGGER.error("Exception writing to internal frame buffer", ex);
            }
            fb.close();
          }
        };
      }

      protected boolean isOneway() {
        return false;
      }

      public void start(I iface, getAnnotatedContent_args args, org.apache.thrift.async.AsyncMethodCallback<AnnotatedDocument> resultHandler) throws TException {
        iface.getAnnotatedContent(args.uri, args.type,resultHandler);
      }
    }

  }

  public static class getAnnotatedContent_args implements org.apache.thrift.TBase<getAnnotatedContent_args, getAnnotatedContent_args._Fields>, java.io.Serializable, Cloneable, Comparable<getAnnotatedContent_args>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getAnnotatedContent_args");

    private static final org.apache.thrift.protocol.TField URI_FIELD_DESC = new org.apache.thrift.protocol.TField("uri", org.apache.thrift.protocol.TType.STRING, (short)1);
    private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.STRING, (short)2);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new getAnnotatedContent_argsStandardSchemeFactory());
      schemes.put(TupleScheme.class, new getAnnotatedContent_argsTupleSchemeFactory());
    }

    public String uri; // required
    public String type; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      URI((short)1, "uri"),
      TYPE((short)2, "type");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 1: // URI
            return URI;
          case 2: // TYPE
            return TYPE;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.URI, new org.apache.thrift.meta_data.FieldMetaData("uri", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getAnnotatedContent_args.class, metaDataMap);
    }

    public getAnnotatedContent_args() {
    }

    public getAnnotatedContent_args(
      String uri,
      String type)
    {
      this();
      this.uri = uri;
      this.type = type;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getAnnotatedContent_args(getAnnotatedContent_args other) {
      if (other.isSetUri()) {
        this.uri = other.uri;
      }
      if (other.isSetType()) {
        this.type = other.type;
      }
    }

    public getAnnotatedContent_args deepCopy() {
      return new getAnnotatedContent_args(this);
    }

    @Override
    public void clear() {
      this.uri = null;
      this.type = null;
    }

    public String getUri() {
      return this.uri;
    }

    public getAnnotatedContent_args setUri(String uri) {
      this.uri = uri;
      return this;
    }

    public void unsetUri() {
      this.uri = null;
    }

    /** Returns true if field uri is set (has been assigned a value) and false otherwise */
    public boolean isSetUri() {
      return this.uri != null;
    }

    public void setUriIsSet(boolean value) {
      if (!value) {
        this.uri = null;
      }
    }

    public String getType() {
      return this.type;
    }

    public getAnnotatedContent_args setType(String type) {
      this.type = type;
      return this;
    }

    public void unsetType() {
      this.type = null;
    }

    /** Returns true if field type is set (has been assigned a value) and false otherwise */
    public boolean isSetType() {
      return this.type != null;
    }

    public void setTypeIsSet(boolean value) {
      if (!value) {
        this.type = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case URI:
        if (value == null) {
          unsetUri();
        } else {
          setUri((String)value);
        }
        break;

      case TYPE:
        if (value == null) {
          unsetType();
        } else {
          setType((String)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case URI:
        return getUri();

      case TYPE:
        return getType();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case URI:
        return isSetUri();
      case TYPE:
        return isSetType();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getAnnotatedContent_args)
        return this.equals((getAnnotatedContent_args)that);
      return false;
    }

    public boolean equals(getAnnotatedContent_args that) {
      if (that == null)
        return false;

      boolean this_present_uri = true && this.isSetUri();
      boolean that_present_uri = true && that.isSetUri();
      if (this_present_uri || that_present_uri) {
        if (!(this_present_uri && that_present_uri))
          return false;
        if (!this.uri.equals(that.uri))
          return false;
      }

      boolean this_present_type = true && this.isSetType();
      boolean that_present_type = true && that.isSetType();
      if (this_present_type || that_present_type) {
        if (!(this_present_type && that_present_type))
          return false;
        if (!this.type.equals(that.type))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public int compareTo(getAnnotatedContent_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = Boolean.valueOf(isSetUri()).compareTo(other.isSetUri());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetUri()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uri, other.uri);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetType()).compareTo(other.isSetType());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetType()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, other.type);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getAnnotatedContent_args(");
      boolean first = true;

      sb.append("uri:");
      if (this.uri == null) {
        sb.append("null");
      } else {
        sb.append(this.uri);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("type:");
      if (this.type == null) {
        sb.append("null");
      } else {
        sb.append(this.type);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class getAnnotatedContent_argsStandardSchemeFactory implements SchemeFactory {
      public getAnnotatedContent_argsStandardScheme getScheme() {
        return new getAnnotatedContent_argsStandardScheme();
      }
    }

    private static class getAnnotatedContent_argsStandardScheme extends StandardScheme<getAnnotatedContent_args> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, getAnnotatedContent_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 1: // URI
              if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                struct.uri = iprot.readString();
                struct.setUriIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            case 2: // TYPE
              if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                struct.type = iprot.readString();
                struct.setTypeIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, getAnnotatedContent_args struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.uri != null) {
          oprot.writeFieldBegin(URI_FIELD_DESC);
          oprot.writeString(struct.uri);
          oprot.writeFieldEnd();
        }
        if (struct.type != null) {
          oprot.writeFieldBegin(TYPE_FIELD_DESC);
          oprot.writeString(struct.type);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class getAnnotatedContent_argsTupleSchemeFactory implements SchemeFactory {
      public getAnnotatedContent_argsTupleScheme getScheme() {
        return new getAnnotatedContent_argsTupleScheme();
      }
    }

    private static class getAnnotatedContent_argsTupleScheme extends TupleScheme<getAnnotatedContent_args> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, getAnnotatedContent_args struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetUri()) {
          optionals.set(0);
        }
        if (struct.isSetType()) {
          optionals.set(1);
        }
        oprot.writeBitSet(optionals, 2);
        if (struct.isSetUri()) {
          oprot.writeString(struct.uri);
        }
        if (struct.isSetType()) {
          oprot.writeString(struct.type);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, getAnnotatedContent_args struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
        BitSet incoming = iprot.readBitSet(2);
        if (incoming.get(0)) {
          struct.uri = iprot.readString();
          struct.setUriIsSet(true);
        }
        if (incoming.get(1)) {
          struct.type = iprot.readString();
          struct.setTypeIsSet(true);
        }
      }
    }

  }

  public static class getAnnotatedContent_result implements org.apache.thrift.TBase<getAnnotatedContent_result, getAnnotatedContent_result._Fields>, java.io.Serializable, Cloneable, Comparable<getAnnotatedContent_result>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getAnnotatedContent_result");

    private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRUCT, (short)0);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new getAnnotatedContent_resultStandardSchemeFactory());
      schemes.put(TupleScheme.class, new getAnnotatedContent_resultTupleSchemeFactory());
    }

    public AnnotatedDocument success; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      SUCCESS((short)0, "success");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 0: // SUCCESS
            return SUCCESS;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, AnnotatedDocument.class)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getAnnotatedContent_result.class, metaDataMap);
    }

    public getAnnotatedContent_result() {
    }

    public getAnnotatedContent_result(
      AnnotatedDocument success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getAnnotatedContent_result(getAnnotatedContent_result other) {
      if (other.isSetSuccess()) {
        this.success = new AnnotatedDocument(other.success);
      }
    }

    public getAnnotatedContent_result deepCopy() {
      return new getAnnotatedContent_result(this);
    }

    @Override
    public void clear() {
      this.success = null;
    }

    public AnnotatedDocument getSuccess() {
      return this.success;
    }

    public getAnnotatedContent_result setSuccess(AnnotatedDocument success) {
      this.success = success;
      return this;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    /** Returns true if field success is set (has been assigned a value) and false otherwise */
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((AnnotatedDocument)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case SUCCESS:
        return getSuccess();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case SUCCESS:
        return isSetSuccess();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getAnnotatedContent_result)
        return this.equals((getAnnotatedContent_result)that);
      return false;
    }

    public boolean equals(getAnnotatedContent_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public int compareTo(getAnnotatedContent_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetSuccess()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
      }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getAnnotatedContent_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
      if (success != null) {
        success.validate();
      }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class getAnnotatedContent_resultStandardSchemeFactory implements SchemeFactory {
      public getAnnotatedContent_resultStandardScheme getScheme() {
        return new getAnnotatedContent_resultStandardScheme();
      }
    }

    private static class getAnnotatedContent_resultStandardScheme extends StandardScheme<getAnnotatedContent_result> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, getAnnotatedContent_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 0: // SUCCESS
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.success = new AnnotatedDocument();
                struct.success.read(iprot);
                struct.setSuccessIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, getAnnotatedContent_result struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.success != null) {
          oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
          struct.success.write(oprot);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class getAnnotatedContent_resultTupleSchemeFactory implements SchemeFactory {
      public getAnnotatedContent_resultTupleScheme getScheme() {
        return new getAnnotatedContent_resultTupleScheme();
      }
    }

    private static class getAnnotatedContent_resultTupleScheme extends TupleScheme<getAnnotatedContent_result> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, getAnnotatedContent_result struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetSuccess()) {
          optionals.set(0);
        }
        oprot.writeBitSet(optionals, 1);
        if (struct.isSetSuccess()) {
          struct.success.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, getAnnotatedContent_result struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
        BitSet incoming = iprot.readBitSet(1);
        if (incoming.get(0)) {
          struct.success = new AnnotatedDocument();
          struct.success.read(iprot);
          struct.setSuccessIsSet(true);
        }
      }
    }

  }

}
